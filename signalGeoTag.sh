#!/usr/bin/env bash


sbt "project noah" "run-main edu.uci.ics.cloudberry.noah.hackJSONTagToADM -b gnosis/src/main/resources/raw/NY/borough.json -n gnosis/src/main/resources/raw/NY/NYNeighbor.json -f noah/src/main/resources/devices.csv
"
