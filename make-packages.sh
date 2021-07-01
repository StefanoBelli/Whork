#!/bin/bash

ORIGIN_APP="target/whork-0.0.0-jar-with-dependencies.jar"
ORIGIN_DOCS="docs/"
ORIGIN_DBSCHEMA="res/createWhorkDbSchema.sql"

WHORK_JAR="whork-app-$GITHUB_SHA-noosx-linuxany-win32oraclejdk8.jar"
WHORK_DOCS="whork-docs-$GITHUB_SHA.zip"
WHORK_DBSCHEMA="createWhorkDbSchema-$GITHUB_SHA.zip"

infopkg() {
    echo "[PACKAGE] Creating package: $1..."
}

donepkg() {
    echo "[PACKAGE] Output: $1"
}

infopkg "app"
mvn assembly:single
mv $ORIGIN_APP ./$WHORK_JAR
donepkg $WHORK_JAR

infopkg "docs"
cd $ORIGIN_DOCS
pdflatex srs.tex
rm -rf srs.log srs.aux srs.tex *.log
cd ..
zip -r $WHORK_DOCS $ORIGIN_DOCS
donepkg $WHORK_DOCS

infopkg "dbschema"
zip -r $WHORK_DBSCHEMA $ORIGIN_DBSCHEMA
donepkg $WHORK_DBSCHEMA
