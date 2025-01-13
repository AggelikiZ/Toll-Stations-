CREATE DATABASE IF NOT EXISTS paywaydb;
USE paywaydb;

CREATE TABLE IF NOT EXISTS User (
    user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE ,
    password VARCHAR(50) NOT NULL,
    user_role ENUM('operator', 'ministry', 'admin') NOT NULL,
    PRIMARY KEY (user_id)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Operator (
    op_id VARCHAR(5) NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    op_name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (op_id),
    CONSTRAINT fk_is_user FOREIGN KEY (user_id) REFERENCES User (user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS TollStation (
    station_id VARCHAR(10) NOT NULL,
    op_id VARCHAR(5) NOT NULL,
    station_type CHAR(2) NOT NULL,
    station_name VARCHAR(255) NOT NULL,
    locality VARCHAR(255) NOT NULL,
    road VARCHAR(255) NOT NULL,
    price1 DECIMAL(4, 2) NOT NULL,
    price2 DECIMAL(4, 2) NOT NULL,
    price3 DECIMAL(4, 2) NOT NULL,
    price4 DECIMAL(4, 2) NOT NULL,
    latitude  DECIMAL(7, 5) NOT NULL,
    longitude DECIMAL(7, 5) NOT NULL,
    PRIMARY KEY (station_id),
    CONSTRAINT fk_operated_by FOREIGN KEY (op_id) REFERENCES Operator (op_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Tag (
    tag_ref VARCHAR(20) NOT NULL,
    op_id VARCHAR(5) NOT NULL,
    PRIMARY KEY (tag_ref),
    CONSTRAINT fk_belongs_to_operator FOREIGN KEY (op_id) REFERENCES Operator (op_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Pass (
    pass_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    pass_time TIMESTAMP NOT NULL,
    tag_ref VARCHAR(20) NOT NULL,
    station_id VARCHAR(10) NOT NULL,
    charge DECIMAL(4, 2) NOT NULL,
    PRIMARY KEY (pass_id),
    CONSTRAINT fk_tag_passed FOREIGN KEY (tag_ref) REFERENCES Tag (tag_ref)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_passed_by FOREIGN KEY (station_id) REFERENCES TollStation (station_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT unique_tag_time UNIQUE (tag_ref, pass_time)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Debt (
    from_op_id VARCHAR(5) NOT NULL,
    to_op_id VARCHAR(5) NOT NULL,
    debt_amount DECIMAL(15, 2) NOT NULL,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (from_op_id, to_op_id),
    CONSTRAINT fk_debt_from_operator FOREIGN KEY (from_op_id) REFERENCES Operator (op_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_debt_to_operator FOREIGN KEY (to_op_id) REFERENCES Operator (op_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Payment (
    payment_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    from_op_id VARCHAR(5) NOT NULL,
    to_op_id VARCHAR(5) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    date DATE NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    details VARCHAR(500),
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_payment_of_debt FOREIGN KEY (from_op_id, to_op_id) REFERENCES Debt (from_op_id, to_op_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)ENGINE = InnoDB;