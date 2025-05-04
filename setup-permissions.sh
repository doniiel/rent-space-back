#!/bin/bash
# Set permissions for configuration and data directories
chmod -R 755 docker-compose/observability docker-compose/postgres_db_data
chmod 600 docker-compose/postgres_db_data/pgpass
chown -R 1001:1001 docker-compose/observability docker-compose/postgres_db_data
chmod +x setup-permissions.sh