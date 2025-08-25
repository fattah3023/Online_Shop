#!/bin/bash

NEW_ADDRESS="https://api.onlineshop.com"

current_version=$(awk -F'[<>]' '/<artifactId>app<\/artifactId>/ {getline; print $3}' app/pom.xml)
major=$(echo "$current_version" | cut -d '.' -f 1)
minor=$(echo "$current_version" | cut -d '.' -f 2)
patch=$(echo "$current_version" | cut -d '.' -f 3 | cut -d '-' -f 1)
patch=$((patch + 1))
new_version="$major.$minor.$patch-RELEASE"
sed -i '' "s/<version>$current_version<\/version>/<version>$new_version<\/version>/" app/pom.xml
echo "Version incremented to: $new_version"

# List of modules
modules=("common" "dataaccess" "service" "app")

# Loop through each module and execute Maven commands
for module in "${modules[@]}"
do
    cd "$module" || exit
    "C:\Program Files\Apache\maven\apache-maven-3.9.11\bin\mvn" clean compile package install
    cd ..
done

