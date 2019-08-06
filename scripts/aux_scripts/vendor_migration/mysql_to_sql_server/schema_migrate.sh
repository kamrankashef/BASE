#!/bin/bash

INPUT=$1
OUTPUT=$2 


echo "Converting ${1}"

if [ "${INPUT}" = "" ] || [ "${OUTPUT}" = "" ]; then
    echo "Usage: mysql_schema/in_path/schema.sql sql_server/out_path/schema.sql  Exiting"
    exit
fi


# Leave schema as is for now -- | sed 's/CREATE TABLE /CREATE TABLE dbo\./g' \

sed 's/DOUBLE/FLOAT/g' $INPUT \
| sed 's/ LONG/ BIGINT/g'\
| sed 's/ ENGINE=InnoDB//g' \
| sed 's/DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP//g' \
| sed 's/TIMESTAMP/DATETIMEOFFSET/g' \
| sed 's/VARCHAR/NVARCHAR/g' \
| sed 's/DATETIME(3)/DATETIMEOFFSET/g' \
| sed 's/BOOLEAN/BIT/g' \
| sed -E 's/VARCHAR\(([0-9]{5,}|((8|9)[0-9]{3,}))\)/VARCHAR(MAX)/' \
> "${OUTPUT}.tmp"

mv "${OUTPUT}.tmp" "${OUTPUT}"

