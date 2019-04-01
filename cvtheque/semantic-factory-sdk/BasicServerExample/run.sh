#!/bin/sh
cd `dirname $0`
KITROOT=../../
CLASSPATH=`echo $KITROOT/javabin/*|sed "s/ /:/g"`
HOSTNAME=madpc125
LD_LIBRARY_PATH=$KITROOT/amd64-linux/lib NGDATADIR=$KITROOT NGINSTALLDIR=$KITROOT NGCONFIGDIR=$KITROOT NGARCH=amd64-linux NGRUNDIR=$KITROOT/run NGHOSTNAME=$HOSTNAME NGRESOURCEPATH=$KITROOT/resource java -classpath $CLASSPATH jexa.bee.Queen --key processName mot --key hostname $HOSTNAME --configuration $KITROOT/examples/BasicServerExample/directory.xml
