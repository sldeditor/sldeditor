#!/bin/sh
OUTPUT_FILE=$1/changelog

rm $OUTPUT_FILE.gz

echo $2' ('$3') stable; urgency=low' > $OUTPUT_FILE
echo '' >> $OUTPUT_FILE
echo '  * Initial Release.' >> $OUTPUT_FILE
echo '' >> $OUTPUT_FILE
echo ' -- '$4' <'$5'> '$(date -R) >> $OUTPUT_FILE

gzip -9 -n $OUTPUT_FILE
