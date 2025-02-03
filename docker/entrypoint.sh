#!/bin/sh

MARIADB_PASSWORD=$(cat "$MARIADB_PASSWORD_FILE")
export MARIADB_PASSWORD

exec java -jar /app/app.jar