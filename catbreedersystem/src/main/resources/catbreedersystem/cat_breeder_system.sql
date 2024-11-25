-- Create the database
CREATE DATABASE cat_breeder_system;

-- Use the created database
USE cat_breeder_system;

-- Drop tables if they already exist
DROP TABLE IF EXISTS Cat;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Reservation;

-- Create the Cat table
CREATE TABLE Cat (
    catID VARCHAR(50) PRIMARY KEY,   -- Use VARCHAR for the cat ID
    name VARCHAR(100),               -- Cat's name
    birthdate DATE,                  -- Cat's birthdate (instead of age)
    gender ENUM('Male', 'Female'),   -- Cat's gender
    color VARCHAR(50),               -- Cat's color
    price INT,                       -- Cat's price
    availability BOOLEAN            -- Availability of the cat
);

-- Create the User table
CREATE TABLE User (
    userID VARCHAR(50) PRIMARY KEY,  -- User ID
    name VARCHAR(100),               -- User's name
    email VARCHAR(100),              -- User's email
    phone VARCHAR(50),               -- User's phone number
    role ENUM('admin', 'customer')   -- User's role: admin or customer
);

-- Create the Reservation table
CREATE TABLE Reservation (
    reserveID VARCHAR(50) PRIMARY KEY,  -- Reservation ID (matches reserveID in Java)
    catID VARCHAR(50),                  -- Foreign key, references CAT table (matches catID in Java)
    userID VARCHAR(50),                 -- Foreign key, references USER table (matches userID in Java)
    reserveDate DATE,                   -- Reservation date (matches reserveDate in Java)
    FOREIGN KEY (catID) REFERENCES Cat(catID),  -- Foreign key constraint
    FOREIGN KEY (userID) REFERENCES User(userID) -- Foreign key constraint
);

-- Insert data into the Cat table
INSERT INTO Cat (catID, name, birthdate, gender, color, price, availability)
VALUES
('0000', 'kiwi', '2024-06-20', 'Male', 'Black', 100, true),
('0001', 'oregen', '2024-07-05', 'Female', 'White', 150, true),
('0002', 'banana', '2024-08-12', 'Male', 'Gray', 120, false),
('0003', 'apple', '2024-09-01', 'Female', 'Tabby', 130, true),
('0004', 'grape', '2024-08-22', 'Male', 'Orange', 140, true),
('0005', 'mango', '2024-07-15', 'Female', 'Cream', 110, false),
('0006', 'peach', '2024-09-30', 'Male', 'Calico', 125, true),
('0007', 'lemon', '2024-10-10', 'Female', 'Tortoiseshell', 160, true),
('0008', 'cherry', '2024-11-05', 'Male', 'Bicolor', 145, false),
('0009', 'plum', '2024-06-30', 'Female', 'Black', 115, true),
('0010', 'watermelon', '2024-07-25', 'Male', 'White', 135, true),
('0011', 'strawberry', '2024-08-05', 'Female', 'Gray', 125, true),
('0012', 'blueberry', '2024-09-10', 'Male', 'Tabby', 140, false),
('0013', 'pear', '2024-08-15', 'Female', 'Orange', 110, true),
('0014', 'pineapple', '2024-11-10', 'Male', 'Cream', 155, false),
('0015', 'coconut', '2024-10-20', 'Female', 'Calico', 145, true),
('0016', 'papaya', '2024-11-02', 'Male', 'Tortoiseshell', 120, true),
('0017', 'apricot', '2024-08-25', 'Female', 'Bicolor', 130, false),
('0018', 'fig', '2024-06-28', 'Male', 'Black', 135, true),
('0019', 'pomegranate', '2024-09-15', 'Female', 'White', 160, true);

-- Insert data into the User table
INSERT INTO User (userID, name, email, phone, role)
VALUES
('0000', 'Dave', 'dave123@gmail.com', '555-0100', 'admin'),
('0001', 'Jack', 'jack456@gmail.com', '555-0101', 'customer'),
('0002', 'Susan', 'susan789@gmail.com', '555-0102', 'customer'),
('0003', 'Michael', 'michael234@gmail.com', '555-0103', 'admin'),
('0004', 'Emily', 'emily345@gmail.com', '555-0104', 'customer'),
('0005', 'David', 'david567@gmail.com', '555-0105', 'customer'),
('0006', 'John', 'john678@gmail.com', '555-0106', 'admin'),
('0007', 'Sarah', 'sarah890@gmail.com', '555-0107', 'customer'),
('0008', 'James', 'james901@gmail.com', '555-0108', 'customer'),
('0009', 'Sophia', 'sophia234@gmail.com', '555-0109', 'admin'),
('0010', 'Olivia', 'olivia345@gmail.com', '555-0110', 'customer'),
('0011', 'Liam', 'liam456@gmail.com', '555-0111', 'customer'),
('0012', 'Ava', 'ava567@gmail.com', '555-0112', 'admin'),
('0013', 'Isabella', 'isabella678@gmail.com', '555-0113', 'customer'),
('0014', 'Mason', 'mason789@gmail.com', '555-0114', 'customer'),
('0015', 'Charlotte', 'charlotte890@gmail.com', '555-0115', 'admin'),
('0016', 'Amelia', 'amelia123@gmail.com', '555-0116', 'customer'),
('0017', 'Benjamin', 'benjamin456@gmail.com', '555-0117', 'customer'),
('0018', 'Lucas', 'lucas789@gmail.com', '555-0118', 'customer'),
('0019', 'Harper', 'harper234@gmail.com', '555-0119', 'customer');

-- Insert data into the Reservation table
INSERT INTO Reservation (reserveID, catID, userID, reserveDate)
VALUES
('R0000', '0000', '0001', '2024-05-30'),
('R0001', '0001', '0002', '2024-06-01'),
('R0002', '0002', '0003', '2024-06-05'),
('R0003', '0003', '0004', '2024-06-07'),
('R0004', '0004', '0005', '2024-06-10'),
('R0005', '0005', '0006', '2024-06-15'),
('R0006', '0006', '0007', '2024-06-20'),
('R0007', '0007', '0008', '2024-06-25'),
('R0008', '0008', '0009', '2024-06-28'),
('R0009', '0009', '0010', '2024-07-02'),
('R0010', '0010', '0011', '2024-07-05'),
('R0011', '0000', '0012', '2024-07-10'),
('R0012', '0001', '0013', '2024-07-12'),
('R0013', '0002', '0014', '2024-07-15'),
('R0014', '0003', '0015', '2024-07-18'),
('R0015', '0004', '0016', '2024-07-20'),
('R0016', '0005', '0017', '2024-07-25'),
('R0017', '0006', '0018', '2024-08-01'),
('R0018', '0007', '0019', '2024-08-05'),
('R0019', '0008', '0001', '2024-08-10');
