#!/bin/bash 
 
display_usage() {  
    echo "must have creds.cnf file with format shown in readme detailing password. Must also have config.xml file present. "
} 



if [  $# -le 1 ] 
	then 
		display_usage
		exit 1
fi 

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )



rm dump.sql

echo "Dumping existing db $1 to dump.sql..."
mysqldump --defaults-file=$SCRIPT_DIR/creds.cnf --add-drop-database	$1 > dump.sql

if [ $? -eq 0 ]; then
    echo "Dump complete"
else
    echo "Dumping failed. Exiting!"
    display_usage
    exit 1;
fi


echo "Creating new db in $2 ..."
mysql --defaults-file=$SCRIPT_DIR/creds.cnf $2 < dump.sql
echo "New db created."


if [ $? -eq 0 ]; then
    echo "New db created."
else
    echo "New db creation failed. Exiting!"
    display_usage
    exit 1;
fi


echo "Anonymising new db based on config.xml"

echo "Running anonymisation script"
$SCRIPT_DIR/anonimatron.sh -config config.xml -synonyms synonyms.xml
echo "Anonymisation complete"


if [ $? -eq 0 ]; then
    echo "Anonymisation complete"
else
    echo "Anonymisation FAILED!"
    display_usage
    exit 1;
fi