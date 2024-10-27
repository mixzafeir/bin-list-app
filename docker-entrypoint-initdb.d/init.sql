CREATE USER IF NOT EXISTS 'etraveli_user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'etraveli_user'@'%';
FLUSH PRIVILEGES;
