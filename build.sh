#!/bin/bash
mvn -DskipTests=true clean package docker:build
