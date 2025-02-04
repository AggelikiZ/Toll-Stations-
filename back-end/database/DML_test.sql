USE paywaydb;
INSERT INTO User (user_id, username, password, user_role) VALUES
    (1, 'aegeanmotorway1', 'default_password', 'operator'),
    (2, 'egnatia2', 'default_password', 'operator'),
    (3, 'gefyra3', 'default_password', 'operator'),
    (4, 'kentrikiodos4', 'default_password', 'operator'),
    (5, 'moreas5', 'default_password', 'operator'),
    (6, 'naodos6', 'default_password', 'operator'),
    (7, 'neaodos7', 'default_password', 'operator'),
    (8, 'olympiaodos8', 'default_password', 'operator'),
    (9, 'admin', '1234', 'admin'),
    (10, 'ypourgeio', 'default_password', 'ministry'),
    (11, 'transportM2', 'default_password', 'ministry');

INSERT INTO Operator (op_id, user_id, op_name, email) VALUES
    ('AM', 1, 'aegeanmotorway', 'customercare@aegeanmotorway.gr'),
    ('EG', 2, 'egnatia', 'eoae@egnatia.gr'),
    ('GE', 3, 'gefyra', 'info@gefyra.gr'),
    ('KO', 4, 'kentrikiodos', 'customercare@kentrikiodos.gr'),
    ('MO', 5, 'moreas', 'info@moreas.com.gr'),
    ('NAO', 6, 'naodos', 'customercare@attikesdiadromes.gr'),
    ('NO', 7, 'neaodos', 'info@neaodos.gr'),
    ('OO', 8, 'olympiaodos', 'customercare@olympiaoperation.gr');