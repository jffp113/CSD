CREATE USER 'develop'@'%' IDENTIFIED BY 'develop';
CREATE database csd;
GRANT ALL PRIVILEGES ON csd.* TO 'develop'@'%';