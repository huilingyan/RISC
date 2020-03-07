#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
scripts/coverage_summary_server.sh
scripts/coverage_summary_client.sh
scripts/coverage_summary_shared.sh
ls -l /
ls -l /coverage-out/
cp -r server/build/reports/clover/html/* /coverage-out/ || exit 1
cp -r client/build/reports/clover/html/* /coverage-out/ || exit 1
cp -r shared/build/reports/clover/html/* /coverage-out/ || exit 1

