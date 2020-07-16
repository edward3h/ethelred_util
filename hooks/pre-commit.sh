#!/bin/bash

TOP_LEVEL_DIR=$(git rev-parse --show-toplevel)
if [ "" = "$TOP_LEVEL_DIR" ]
then
    TOP_LEVEL_DIR="."
fi

# format
$TOP_LEVEL_DIR/gradlew googleJavaFormat

# test
$TOP_LEVEL_DIR/gradlew test