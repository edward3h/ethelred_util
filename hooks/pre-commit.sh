#!/bin/bash

TOP_LEVEL_DIR=$(git rev-parse --show-toplevel)
if [ "" = "$TOP_LEVEL_DIR" ]
then
    TOP_LEVEL_DIR="."
fi

# format
# check and format everything regardless of which files changed
if ! ./gradlew verifyGoogleJavaFormat
then
$TOP_LEVEL_DIR/gradlew googleJavaFormat
# block commit if files were reformatted
exit 1 
fi

# test
$TOP_LEVEL_DIR/gradlew test