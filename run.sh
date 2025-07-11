#!/bin/bash

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "Error: Maven is not installed. Please install it before running this script."
    exit 1
fi

# Clean and build the project
echo "Building project..."
mvn clean package

# Check if the build was successful
if [ $? -ne 0 ]; then
    echo "Build error. Please check the error messages."
    exit 1
fi

# Run the application
echo "Starting application..."
java -cp target/classes hashj.HashJ
