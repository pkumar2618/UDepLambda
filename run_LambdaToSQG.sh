#!/usr/bin/env bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home
PATH=${JAVA_HOME}/bin:${PATH}

cd /Users/pawan/projects/UDepLambda/

#---------------------------------#
# dynamically build the classpath #
#---------------------------------#
THE_CLASSPATH=
for i in `ls ./lib/*.jar`
do
  THE_CLASSPATH=${THE_CLASSPATH}:${i}
done

THE_CLASSPATH=${THE_CLASSPATH}:/Users/pawan/projects/UDepLambda/bin

cat dep_parse.txt |
java -Dfile.encoding="UTF-8" -cp ".:${THE_CLASSPATH}" deplambda.cli.RunLambdaToSQG