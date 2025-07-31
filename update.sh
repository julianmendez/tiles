#!/bin/bash

# This script translates the Soda source files into Lean.
#
# 2024-02-18

oldIFS="$IFS"
IFS=$'\n'
# $IFS is the Internal Field Separator.
# Its default value is <space><tab><newline>.
# This is changed to support spaces in file names.


pathToSodaDir="core/src/main/scala/soda/tiles/fairness"
pathToLeanDir="Soda/tiles/fairness"


# This translates all Soda files into Lean
packages=$(ls "${pathToSodaDir}")

for package in ${packages}; do
  files=$(ls "${pathToSodaDir}/${package}")
  mkdir -p "${pathToLeanDir}/${package}"

  for file in ${files}; do

    if [ "${file: -5}" == ".soda" ]; then
      fileName="${file%.*}"
      soda lean "${pathToSodaDir}/${package}/${fileName}.soda" "${pathToLeanDir}/${package}/${fileName}.lean"
    fi

  done

done

# This updates elan
elan self update

# This updates lake
lake update


IFS="$oldIFS"

