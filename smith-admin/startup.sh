#!/bin/bash
PROJECT_HOME="/var/java"
cd $PROJECT_HOME


start(){
        exec java ${JAVA_OPTS} -jar target/data-smith-admin.jar
}
start
