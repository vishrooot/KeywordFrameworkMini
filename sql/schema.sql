CREATE DATABASE IF NOT EXISTS keyword_framework;
USE keyword_framework;

CREATE TABLE action (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE locator (
    id INT AUTO_INCREMENT PRIMARY KEY,
    locator_value VARCHAR(255) NOT NULL
);

CREATE TABLE testcase (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE testcase_steps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    testcase_id VARCHAR(20),
    step_number INT,
    action_id INT,
    locator_id INT,
    testdata VARCHAR(255),
    FOREIGN KEY (testcase_id) REFERENCES testcase(id),
    FOREIGN KEY (action_id) REFERENCES action(id),
    FOREIGN KEY (locator_id) REFERENCES locator(id)
);