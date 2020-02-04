#!/bin/zsh
# Author: @david.zhao
# Date: 02/04/2020

export FLOW_PATH="/home/davidzhao/resentek/opensource/micronaut-starter-kit/src/main/resources"
export OUT_PATH="/home/davidzhao/resentek/opensource/micronaut-starter-kit/build"

if (test "$2" != ""); then
  echo -- ./gradlew run $1 $2 --
  ./gradlew run --args="-p:$FLOW_PATH -f:$1 -o:$OUT_PATH/$1.md" $2
else
  echo -- ./gradlew run $1 -q --
  ./gradlew run --args="-p:$FLOW_PATH -f:$1 -o:$OUT_PATH/$1.md" -q
fi
echo -- end of run --
