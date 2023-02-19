#!/bin/bash

# this file exists because my macos java setup broke

#PLATFORM=${PLATFORM:-"linux/amd64"}
#BUILD=${BUILD:-0}
dir_name="/$(basename $PWD)"
hostip="${HOST_IP:-$(ipconfig getifaddr en0)}"
img_name="eclipse-temurin:8-jdk"

#if [[ $BUILD == "1" ]]; then
#    docker build --platform $PLATFORM -t builder .
#fi
#docker run --platform $PLATFORM --rm  -v "$PWD":"$dir_name" -w "$dir_name" -e GRADLE_USER_HOME="$dir_name/opt/gradle_user_home" -e DISPLAY="$hostip:0" -it --name builder_run builder "${@}"

docker run --rm  -v "$PWD":"$dir_name" -w "$dir_name" --entrypoint "$dir_name/entrypoint.sh" -e DISPLAY="$hostip:0" -it --name builder_run $img_name "${@}"
