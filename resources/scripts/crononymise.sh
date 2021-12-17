#!/bin/bash 
 
display_usage() {
    echo "Usage: <db_to_anonymise>"  
    echo "Note: must have creds.cnf file with format shown in readme detailing password. Must also have config.xml file present. "
    rm temp_dump.sql
} 



if [  $# -le 1 ] 
	then 
		display_usage
		exit 1
fi 

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
TEMP_DB=$(grep -o "jdbc:mysql://.*:\d\+/[A-Za-z_]\+" $SCRIPT_DIR/config.xml | sed 's/jdbc:mysql:\/\/.*:[0-9]*\///g')
echo "Using $TEMP_DB as temporary schema for anonymisation process."

if [ $TEMP_DB -eq "cronomet_nutrimeter"]; then 
    echo "YOU SHALL NOT PASS! Don't try to use the main db for anonymisation you fool."
    exit 1;
fi

rm temp_dump.sql

echo "Dumping existing db $1 to dump.sql..."
mysqldump --defaults-file=$SCRIPT_DIR/creds.cnf --add-drop-database	$1 > temp_dump.sql

if [ $? -eq 0 ]; then
    echo "Dump complete"
else
    echo "Dumping failed. Exiting!"
    display_usage
    exit 1;
fi


echo "Creating new db in $2 ..."
mysql --defaults-file=$SCRIPT_DIR/creds.cnf $2 < temp_dump.sql


if [ $? -eq 0 ]; then
    echo "New db created."
else
    echo "New db creation failed. Exiting!"
    display_usage
    exit 1;
fi

echo "Removing temporary dump."
rm temp_dump.sql


echo "Anonymising new db based on config.xml"

echo "Running anonymisation script"
$SCRIPT_DIR/anonimatron.sh -config config.xml -synonyms synonyms.xml


if [ $? -eq 0 ]; then
    echo "Anonymisation complete"
else
    echo "Anonymisation FAILED!"
    display_usage
    exit 1;
fi

mysqldump --defaults-file=$SCRIPT_DIR/creds.cnf $2 > anonymised_dump.sql

