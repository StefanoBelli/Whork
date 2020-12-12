#!/bin/bash

NOOUT="2>/dev/null >>/dev/null"

ORIGIN_APP="target/whork-0.0.0-jar-with-dependencies.jar"
ORIGIN_DOCS="docs/"
ORIGIN_DBSCHEMA="res/createWhorkDbSchema.sql"

WHORK_JAR="whork-app-$TRAVIS_TAG-noosx-linuxany-win32oraclejdk8.jar"
WHORK_DOCS="whork-docs-$TRAVIS_TAG.zip"
WHORK_DBSCHEMA="createWhorkDbSchema-$TRAVIS_TAG.zip"

infopkg() {
    echo "[PACKAGE] Creating package: $1..."
}

donepkg() {
    echo "[PACKAGE] Output: $1"
}

infopkg "app"
mvn assembly:single $NOOUT
mv $ORIGIN_APP ./$WHORK_JAR $NOOUT
donepkg $WHORK_JAR

infopkg "docs"
cd $ORIGIN_DOCS
pdflatex srs.tex $NOOUT
rm -rf srs.log srs.aux srs.tex *.log
cd ..
zip -r $WHORK_DOCS $ORIGIN_DOCS
donepkg $WHORK_DOCS

infopkg "dbschema"
zip -r $WHORK_DBSCHEMA $ORIGIN_DBSCHEMA
donepkg $WHORK_DBSCHEMA