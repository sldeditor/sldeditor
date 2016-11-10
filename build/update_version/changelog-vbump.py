#! /usr/bin/env python
#
# coding: utf-8
"""
Version-bump script for github_changelog_generator.
Reads .github_changelog_generator, parses the future version, increments it and writes an updated file to stdout.

Usage:
    changelog-vbump.py [-i] [-v <new version number>] [path to .github_changelog_generator]

Options:
    -n  Dry run: just print the new version number that would be written to .github_changelog_generator
    -i  Edit .github_changelog_generator in place, instead of writing result to stdout
    -v  specify a version number, e.g. "1.23"

If the next version number (v_n) is not specified, it will be guestimated from
the current version (v_c) using the following rules:
    - if v_c is of the form 'a-SNAPSHOT',
        remove the '-SNAPSHOT' part, so v_n = a
    - if v_c is of the form 'a.b.c...z' where z is an integer,
        increment z and add '-SNAPSHOT', so v_n = a.b.c...(z+1)-SNAPSHOT
    - otherwise give up and throw an error

If .github_changelog_generator file is not specified, the script will look in the current working directory.

Tested with Python 2.7.5 and 3.3.2.
Requires lxml.
"""
import sys
import getopt
import os.path
from lxml import etree as ET

class InvalidVersion(Exception):
    def __init__(self, msg):
        self.msg = msg
    def __str__(self):
        return "Invalid version: " + self.msg

def main(args):
    next_version = None
    in_place = False
    dry_run = False
    input_file = './.github_changelog_generator'

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
        log("ERROR: Could not find .github_changelog_generator file: %s" %input_file)
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
    
    field = 'future-release='
    index = -1
    found = False
    for line in array:
        index = index + 1
        if line.startswith(field):
            found = True
            break

    if found == False:
        raise InvalidVersion("Failed to find future-release")

    current_version = array[index][len(field):]
    
    log("Current version is %s" % current_version)

    # Calculate the next version, if not specified
    if not next_version:
        next_version = increment_version(current_version)

    # If dry run, just print the next version and exit
    if dry_run:
        print next_version
    else:
        log("Incrementing version to %s" % next_version)

        # Update the content
        array[index] = field + next_version
        contents = ''.join(array).replace('\n', '\r\n') + '\r\n'
        if in_place:
            # Write back to file
            write_to_file(contents, input_file)
        else:
            # Print result to stdout
            print_contents(contents)

def increment_version(current_version):
    # If it's a snapshot version, convert it to a release version
    if current_version.endswith('-SNAPSHOT'):
        return current_version[:-9]

    parts = current_version.split(".")
    last_part = parts[len(parts) - 1]
    try:
        # Add one to the final part of the version string
        incremented_last_part = str(int(last_part) + 1)
    except TypeError:
        raise InvalidVersion("Unsuppported version format [%s]" % current_version)

    # Try to maintain the zero padding of the old version, if any
    incremented_last_part = incremented_last_part.zfill(len(last_part))

    # Make it a snapshot version
    incremented_last_part = incremented_last_part + '-SNAPSHOT'

    return ".".join(parts[:-1] + [incremented_last_part])

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