#! /bin/sh

if [ $# -lt 1 ]; then
    echo "usage: $0 /path/to/kit/install/dir" >&2
    exit 1
fi

KITROOT=$1
shift
CLASSPATH=`echo $KITROOT/javabin/*.jar | tr ' ' ':'`
LD_LIBRARY_PATH=$KITROOT/amd64-linux/lib NGRESOURCEPATH=$KITROOT/resource/all-arch java -cp $PWD/bin:$CLASSPATH com.exalead.semanticfactory.example.MyTextProcessingPipeline $@
