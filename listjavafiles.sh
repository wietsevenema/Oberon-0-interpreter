#!/bin/bash
find . -iname "*.java" -and -not -path "*parser/Oberon.java" -and -not -path "*libs*" -and -not -path "*tests*" > files.list

