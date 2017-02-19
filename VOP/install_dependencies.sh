#!/bin/bash

# Ensure add-apt-repository is installed
apt-get install software-properties-common python-software-properties 

# Install PostgreSQL, Java 8 and Gradle
add-apt-repository ppa:webupd8team/java
apt-get update
apt-get install oracle-java8-installer
apt-get install postgresql

# Install dependencies for the setup_backend.py script
apt-get install postgresql-client libpq-dev libffi-dev python python-dev python-pip
pip install psycopg2
pip install click
pip install bcrypt

echo "----"
echo "Installing dependencies complete. However, gradle requires manual installation"
echo "Please manually install gradle using the instructions at gradle.org/docs/current/userguide/installation.html"
echo "----"
