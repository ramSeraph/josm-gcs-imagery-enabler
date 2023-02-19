#!/bin/bash

export PATH="${PATH}:${PWD}/opt/gradle-8.0/bin/"
export GRADLE_USER_HOME="${PWD}/opt/gradle_home"
export GITHUB_ACCESS_TOKEN="$(cat ${PWD}/token.txt)"

exec ${@}

