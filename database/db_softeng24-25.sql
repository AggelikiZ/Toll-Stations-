CREATE DATABASE db_softeng24-25;
USE db_softeng24-25;

-- Operators table
CREATE TABLE Operators (
    opID VARCHAR(50) PRIMARY KEY,
    operator VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Tolls stations table
CREATE TABLE TollStations (
    tollID VARCHAR(10) PRIMARY KEY,
    opID VARCHAR(50) PRIMARY KEY,
    toll_name VARCHAR(255) NOT NULL,
    locality VARCHAR(255) NOT NULL,
    road VARCHAR(255) NOT NULL,
    price1 DECIMAL(3, 2) NOT NULL,
    price2 DECIMAL(4, 2) NOT NULL,
    price3 DECIMAL(4, 2) NOT NULL,
    price4 DECIMAL(4, 2) NOT NULL,
    latitude  DECIMAL(7, 5) NOT NULL,
    longitude DECIMAL(7, 5) NOT NULL,
    FOREIGN KEY (opID) REFERENCES Operators(opID)
);

-- Tags table
CREATE TABLE Tags (
    tagRef VARCHAR(50) PRIMARY KEY,
    ptagHomeID VARCHAR(50) NOT NULL,
    balance DECIMAL(6, 2) DEFAULT 0.0,
    FOREIGN KEY (tagHomeID) REFERENCES Operators(operatorID)
);

-- Passes table
CREATE TABLE Passes (
    passID INT AUTO_INCREMENT PRIMARY KEY,
    timestamp_ID DATETIME NOT NULL,
    tollID VARCHAR(50) NOT NULL,
    tagHomeID VARCHAR(50) NOT NULL,
    tagRef VARCHAR(50) NOT NULL,
    charge DECIMAL(4, 2) NOT NULL,
    FOREIGN KEY (tagRef) REFERENCES Tags(tagRef),
    FOREIGN KEY (tollID) REFERENCES TollStations(tollID),
    FOREIGN KEY (tagHomeID) REFERENCES Tags(tagHomeID)
);

-- Transactions between operators table
CREATE TABLE Transactions (
    transactionID INT AUTO_INCREMENT PRIMARY KEY,
    fromOperatorID VARCHAR(50) NOT NULL,
    toOperatorID VARCHAR(50) NOT NULL,
    amount FLOAT NOT NULL, -- float type to be discussed
    transactionDate DATE NOT NULL,
    FOREIGN KEY (fromOperatorID) REFERENCES Operators(opeID),
    FOREIGN KEY (toOperatorID) REFERENCES Operators(opID)
);

-- Users table
CREATE TABLE Users (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    user_role ENUM('admin', 'user') DEFAULT 'user'
);
