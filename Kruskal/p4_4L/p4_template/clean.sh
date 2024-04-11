#!/bin/bash

# Remove all .class files in the current directory and its subdirectories
find . -name "*.class" -exec rm -f {} \;

echo "All .class files deleted successfully."