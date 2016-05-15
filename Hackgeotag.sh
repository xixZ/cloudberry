#!/usr/bin/env bash


sbt "project noah" "run-main edu.uci.ics.cloudberry.noah.hackJSONTagToADM -n gnosis/src/main/resources/raw/NY/tier2bound.geojson -f noah/src/main/resources/mobile_signal_info_all_september.csv
"
