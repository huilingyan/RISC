#!/bin/bash

./gradlew build || exit 1
./gradlew cloverAggregateReport || exit 1

emacs --batch -u `whoami` --script scripts/docov.el

cv=`egrep "\| *Totals *\|" coverage.txt | cut -f 3 -d"|" | tr -d " "`

echo "TOTAL COVERAGE: ${cv}%"

ls -l /
ls -l /coverage-out/
cp -r build/reports/clover/html/* /coverage-out/ || exit 1
#cp -r client/build/reports/clover/html/* /coverage-out/ || exit 1
#cp -r shared/build/reports/clover/html/* /coverage-out/ || exit 1

