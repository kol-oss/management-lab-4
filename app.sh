#!/bin/sh

MODE="$1"

if [ "$MODE" = "test" ]; then
    mvn clean test
elif [ "$MODE" = "file" ]; then
    INPUT_PATH="$2"

    if [ -z "$2" ]; then
      echo "[WARNING] Input file is not specified, used src/main/resources/input.txt"
    fi

    OUTPUT_PATH="$3"

    if [ -z "$3" ]; then
      echo "[WARNING] Output file is not specified, used result.txt"
    fi

    if [ -z "$4" ]; then
      echo "[WARNING] Steps mod is not specified, not used"
    fi

    mvn exec:java -Dexec.args="FILE $INPUT_PATH $OUTPUT_PATH $4"
elif [ "$MODE" = "console" ]; then
    if [ -z "$2" ]; then
      echo "[WARNING] Steps mod is not specified, not used"
    fi

    mvn exec:java -Dexec.args="CONSOLE $2"
else
    echo "[ERROR] Unknown mode (allowed: test/file/console)"
fi
