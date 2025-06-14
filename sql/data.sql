USE keyword_framework;

INSERT INTO action (name) VALUES ('open'), ('type'), ('click');

INSERT INTO locator (locator_value) VALUES 
    (''),
    ('//input[@name="q"]'),
    ('//input[@type="submit"]');

INSERT INTO testcase (id, name) VALUES ('TC001', 'Google Search');

INSERT INTO testcase_steps (testcase_id, step_number, action_id, locator_id, testdata) VALUES
    ('TC001', 1, 1, 1, 'https://www.google.com'),
    ('TC001', 2, 2, 2, 'Selenium WebDriver'),
    ('TC001', 3, 3, 3, '');