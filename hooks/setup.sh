#!/bin/sh

#
# A single script to install all the custom GIT hooks
#

HOOK_NAMES="pre-commit"

TOP_LEVEL_DIR=$(git rev-parse --show-toplevel)
if [ "" = "$TOP_LEVEL_DIR" ]
then
    TOP_LEVEL_DIR="."
fi
HOOK_DIR="$TOP_LEVEL_DIR/.git/hooks"
echo $HOOK_DIR

if [ -d "$HOOK_DIR" ]
then
    for hook in $HOOK_NAMES; do
        ln -s -f $TOP_LEVEL_DIR/hooks/$hook.sh $HOOK_DIR/$hook
    done
fi