Create database cat_breeder_system;

Use cat_breeder_system; 

/*
Drop table Cat;
Drop table User;
Drop table Reservation ;
 */


-- Create the Cat table
CREATE TABLE Cat (
    catID VARCHAR(50) PRIMARY KEY,   -- Use VARCHAR for the cat ID
    name VARCHAR(100),                 -- Cat's name
    age INT,                           -- Cat's age
    gender ENUM('Male', 'Female'),     -- Cat's gender
    color VARCHAR(50),                 -- Cat's color
    price INT,                         -- Cat's price
    availability BOOLEAN              -- Availability of the cat
);

-- Create the User table
CREATE TABLE User (
    userID VARCHAR(50) PRIMARY KEY,    -- User ID
    name VARCHAR(100),                  -- User's name
    email VARCHAR(100),                 -- User's email
    phone VARCHAR(50),                  -- User's phone number
    role ENUM('admin', 'customer')      -- User's role: admin or customer
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



