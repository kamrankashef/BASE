#!/bin/bash

SOURCE_DIR=$1
OUT_FILE=$2
SCHEMA=$3

# Use of -E is for extended regexp in sed
# Use of $ is need for newlines in sed
# Need to use [ ] instead of \s for space character class in OSX

find "${SOURCE_DIR}" -name create.sql -type f -exec cat {} \; -exec echo " ;" \; \
| sed -e 's/) UNLOAD.*;/);/g' \
| grep -vi "with parameters" \
| sed -e $'s/\([^"]\), /\\1,\\\n/g' \
| sed -e 's/CS_INT//g' -e 's/CS_DOUBLE//g' -e 's/CS_LONGDATE//g' -e 's/CS_FIXED//g' -e 's/CS_DAYDATE//g' -e 's/CS_STRING//g' \
| sed -e 's/DOUBLE/FLOAT/g' \
| sed -e $'s/UNLOAD PRIORITY 5  AUTO MERGE/;\\\n/' \
| sed -e 's/LONGDATE/DATETIME/g' \
| sed -e 's/DAYDATE/DATE/g' \
| grep -vi "comment on column" \
| sed  -E "s/(COLUMN|ROW)? TABLE \"Insight(_Staging)?\"/TABLE \"\\${SCHEMA}\"/" \
| sed -E $'s/TABLE "([^"]+)"."([^"]+)" \(/TABLE "\\1"."\\2" \(\\\n/g' \
| sed -E $'s/\)\)/\)\\\n\)/g' \
| sed -E 's/^[ ]*;[ ]*$//g' \
> "${OUT_FILE}"
