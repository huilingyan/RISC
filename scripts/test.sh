#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
cd server 

emacs --batch -u `whoami` --script ../scripts/docov.el

cv=`egrep "\| *Totals *\|" coverage.txt | cut -f 3 -d"|" | tr -d " "`

echo "TOTAL COVERAGE: ${cv}%"

cd ..
cd client 

emacs --batch -u `whoami` --script ../scripts/docov.el

cv=`egrep "\| *Totals *\|" coverage.txt | cut -f 3 -d"|" | tr -d " "`

echo "TOTAL COVERAGE: ${cv}%"

cd ..
cd shared 

emacs --batch -u `whoami` --script ../scripts/docov.el

cv=`egrep "\| *Totals *\|" coverage.txt | cut -f 3 -d"|" | tr -d " "`

echo "TOTAL COVERAGE: ${cv}%"

cd ..
ls -l /
ls -l /coverage-out/
cp -r server/build/reports/clover/html/* /coverage-out/ || exit 1
cp -r client/build/reports/clover/html/* /coverage-out/ || exit 1
cp -r shared/build/reports/clover/html/* /coverage-out/ || exit 1

