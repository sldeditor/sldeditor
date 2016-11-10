#! /usr/bin/env python
#
# coding: utf-8
"""
Version-bump script for README.md.
Reads README.md, parses the future version, increments it and writes an updated file to stdout.

Usage:
    readme-vbump.py [-i] [-v <new version number>] [path to README.md]

Options:
    -n  Dry run: just print the new version number that would be written to README.md
    -i  Edit README.md in place, instead of writing result to stdout
    -v  specify a version number, e.g. "1.23"

If the next version number (v_n) is not specified, it will be guestimated from
the current version (v_c) using the following rules:
    - if v_c is of the form 'a-SNAPSHOT',
        remove the '-SNAPSHOT' part, so v_n = a
    - if v_c is of the form 'a.b.c...z' where z is an integer,
        increment z and add '-SNAPSHOT', so v_n = a.b.c...(z+1)-SNAPSHOT
    - otherwise give up and throw an error

If README.md file is not specified, the script will look in the current working directory.

"""
import sys
import getopt
import os.path
import hashlib
import datetime

download = 'https://github.com/robward-scisys/sldeditor/releases/download/v{}/SLDEditor.jar)'

class InvalidVersion(Exception):
    def __init__(self, msg):
        self.msg = msg
    def __str__(self):
        return "Invalid version: " + self.msg

def main(args):
    next_version = None
    in_place = False
    dry_run = False
    input_file = './README.md'

    try:
        opts, args = getopt.getopt(args, 'nv:ih', ['dryrun', 'version', 'inplace', 'help'])
    except getopt.GetoptError:
        usage()
        return False

    for opt, value in opts:
        if opt in ('-h', '--help'):
            usage()
            return False
        elif opt in ('-v', '--version'):
            next_version = value
            if next_version.endswith('-SNAPSHOT'):
                next_version = next_version[:-9]

        elif opt in ('-i', '--inplace'):
            in_place = True
        elif opt in ('-n', '--dryrun'):
            dry_run = True
        else:
            usage()
            return False

    if len(args) > 0:
        input_file = args[0]
    if not os.path.isfile(input_file):
        log("ERROR: Could not find README.md file: %s" %input_file)
        usage()
        return False

    try:
        bump(input_file, next_version, in_place, dry_run)
        return True
    except InvalidVersion as e:
        log(e)
        return False

def usage():
    print(__doc__)

def bump(input_file, next_version, in_place, dry_run):
    log("Parsing %s..." % input_file)

    array = []
    with open(input_file, "r") as ins:
        for line in ins:
            array.append(line)
    
    fields = ['[![GitHub release]', '[![Github All Releases]' ]
    release_field = '* [SLDEditor Release '
    index = -1
    for line in array:
        index = index + 1
        for field in fields:
            if line.startswith(field):
                array[index] = update(line, next_version)
        if line.startswith(release_field):
            array[index] = update_release(line, next_version)


    # Update the content
    contents = ''.join(array).replace('\n', '\r\n') + '\r\n'
    if in_place:
        # Write back to file
        write_to_file(contents, input_file)
    else:
        # Print result to stdout
        print_contents(contents)

def hashfile(afile, hasher, blocksize=65536):
    buf = afile.read(blocksize)
    while len(buf) > 0:
        hasher.update(buf)
        buf = afile.read(blocksize)
    return hasher.hexdigest()

def update(line, next_version):
    # If it's a snapshot version, convert it to a release version
    if next_version.endswith('-SNAPSHOT'):
        next_version = next_version[:-9]

    start = line.find('(https://github')
    if start == -1:
        return line

    line = line[:start] + download.format(next_version)
    print line

    return line

def update_release(line, next_version):
    # If it's a snapshot version, convert it to a release version
    if next_version.endswith('-SNAPSHOT'):
        next_version = next_version[:-9]

    start = line.find('(https://github')
    if start == -1:
        return line

    full_download = download.format(next_version)
    fname = '../../bin/SLDEditor.jar'
    md5 = hashfile(open(fname, 'rb'), hashlib.md5())
    date_string = datetime.date.today().strftime('%d %b %Y')
    line = '* [SLDEditor Release {}]({}) (MD5 : {}) Released {}'.format(next_version, full_download, md5, date_string);

    print line

    return line

def write_to_file(contents, output_file):
    with open(output_file, 'wb') as f:
        f.write(contents)

def print_contents(contents):
    if sys.hexversion >= 0x03000000:
        # Python 3.x
        sys.stdout.buffer.write(contents)
    else:
        # Python 2.x
        print(contents)

def log(msg):
    sys.stderr.write(str(msg) + '\n')

if __name__ == '__main__':
    sys.exit(not main(sys.argv[1:]))