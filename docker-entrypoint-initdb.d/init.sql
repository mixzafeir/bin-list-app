-- Create a new user 'myuser' with password 'mypassword'
CREATE USER IF NOT EXISTS 'etraveli_user'@'%' IDENTIFIED BY 'password';

-- Grant all privileges to 'myuser' on all databases
GRANT ALL PRIVILEGES ON *.* TO 'etraveli_user'@'%';

-- Apply the changes
FLUSH PRIVILEGES;
