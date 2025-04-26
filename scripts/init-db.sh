#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status.

# Perform database creation using the POSTGRES_USER defined in docker-compose
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
    SELECT 'CREATE DATABASE user_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_db')\gexec
    SELECT 'CREATE DATABASE product_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'product_db')\gexec
    SELECT 'CREATE DATABASE inquiry_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'inquiry_db')\gexec
EOSQL
echo "Databases created (if they didn't exist)."