#!/bin/sh
KITROOT=`dirname $0`/../../
CLASSPATH=`echo $KITROOT/javabin/*|sed "s/ /:/g"`
LD_LIBRARY_PATH=$KITROOT/amd64-linux/lib NGRESOURCEPATH=$KITROOT/resource java -classpath $CLASSPATH com.exalead.mot.examples.BasicServletExample $@
