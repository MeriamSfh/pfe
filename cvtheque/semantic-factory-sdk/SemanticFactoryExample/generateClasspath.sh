#! /bin/bash

if [ $# -ne 1 ]; then
    echo "usage: $0 /path/to/kit/install/dir" >&2
    exit 1
fi

classpath=`dirname $0`/.classpath

cat <<EOF > $classpath
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="output" path="bin"/>
EOF

for i in `ls $1/javabin/*.jar`; do
    echo "	<classpathentry kind='lib' path='$i' />" >> $classpath
done

echo "</classpath>" >> $classpath
