#!/bin/bash

# 清理
echo "clean sdk..."
mvn clean -f  pom.xml > /dev/null

echo "clean examples..."
mvn clean -f ./examples/pom.xml > /dev/null

# install
echo "install sdk..."
mvn install -f pom.xml > null

# package
echo "package example..."
mvn package -f ./examples/pom.xml > /dev/null

# test
echo "start test jar"
java -jar examples/target/vectordb-example.jar 111
