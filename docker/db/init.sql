SELECT 'CREATE DATABASE kc_pitanga'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'kc_pitanga')\gexec
