#! /bin/bash
# mvn clean site deploy -DperformRelease=true -Dmaven.javadoc.skip=true -e
mvn clean deploy -DperformRelease=true -e
