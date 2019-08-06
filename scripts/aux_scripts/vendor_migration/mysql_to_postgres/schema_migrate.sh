#!/bin/bash

INPUT=$1
OUTPUT=$2 

if [ "${INPUT}" = "" ] || [ "${OUTPUT}" = "" ]; then
    echo "Usage: mysql_schema/in_path/schema.sql sql_server/out_path/schema.sql  Exiting"
    exit
fi


# Leave schema as is for now -- | sed 's/CREATE TABLE /CREATE TABLE dbo\./g' \

sed 's/DOUBLE/DOUBLE PRECISION/g' $INPUT \
| sed 's/ LONG/ BIGINT/g'\
| sed 's/ ENGINE=InnoDB//g' \
| sed 's/DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP//g' \
| sed 's/TIMESTAMP/TIMESTAMP WITH TIME ZONE/g' \
| sed 's/TIMESTAMP WITH TIME ZONE(3)/TIMESTAMP WITH TIME ZONE/g' \
| sed 's/DATETIME(3)/TIMESTAMP WITH TIME ZONE /g' \
> $OUTPUT

