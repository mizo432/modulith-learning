-- Add first_login column to users table
ALTER TABLE users
    ADD COLUMN first_login BOOLEAN NOT NULL DEFAULT false;
