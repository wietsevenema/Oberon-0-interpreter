#!/bin/bash
find . -iname "*.java" -and -not -path "*parser/Oberon.java" -and -not -path "*libs*" -and -not -path "*metrics*" -and -not -path "*tests*" -and -not -iname "ModulePrinter*" > files.list

