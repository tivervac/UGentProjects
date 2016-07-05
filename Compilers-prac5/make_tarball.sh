#!/bin/bash -ue

PRACT=5

if [ $# -ne 1 ]
then
    echo "Usage: $0 <GROUP>" 1>&2
    exit 1
fi
GROUP=$1

FILENAME=$(printf 'pract%.2i_group%.2i.tar.gz' $PRACT $GROUP)
FILES="sumsqr_annotated.im deel1bc.txt deel2abc.txt"

tar -czvf ${FILENAME} ${FILES}
echo
echo Done, tarball is named \'${FILENAME}\'
