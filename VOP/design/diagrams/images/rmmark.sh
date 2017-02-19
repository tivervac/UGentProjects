#! /bin/bash

# first argument is dir containing svg images
dir="$1"

# no image folder specified
[ "$#" -ne 1 ] && >&2 echo "E: incorrect number of parameters, image directory should be specified!" && exit 1

# check if image dir exists
[ ! -d "$dir" ] && >&2 echo "E: invalid image folder" && exit 2

# remove watermark
sed -i 's/Visual Paradigm Professional Edition .evaluation copy.//' "${dir}"/*svg

# convert svg to png <3 POSIX
find "$dir" -type f -name "*svg" -exec sh -c 'convert "$1" "${1%.svg}.png"' _ {} \;

# success
exit 0
