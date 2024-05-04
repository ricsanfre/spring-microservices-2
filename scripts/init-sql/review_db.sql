CREATE USER review with encrypted password 'password';
CREATE DATABASE IF NOT EXISTS review OWNER review;
GRANT ALL PRIVILEGES ON DATABASE review TO review;