#!/bin/bash

./run.sh ./gradlew clean build
jar_file="build/localDist/gcsenabler-dev.jar"
firstline="$(basename $jar_file);file:$(realpath $jar_file)"

list_file=build/localDist/list
tmp_file=build/localDist/list.new
echo $firstline > $tmp_file
tail +2 build/localDist/list >> $tmp_file
mv $tmp_file $list_file
