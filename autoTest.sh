#!/bin/bash

echo "vdb_url="$1
echo "vdb_key="$2
echo "log_path="$3
# 清理
echo "clean sdk..."
mvn clean -f  pom.xml > /dev/null

echo "clean examples..."
mvn clean -f ./examples/pom.xml > /dev/null

# install
echo "install sdk..."
mvn install -f pom.xml > /dev/null

# package
echo "package example..."
mvn package -f ./examples/pom.xml > /dev/null

# test
echo "start test jar"
# 需要 pom 文件的 build 标签有 <finalName>vectordb-example</finalName>n
java -Dvdb_url=$1 -Dvdb_key=$2 -jar examples/target/vectordb-example.jar > $3
