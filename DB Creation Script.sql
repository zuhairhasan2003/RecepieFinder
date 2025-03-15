CREATE DATABASE IF NOT EXISTS RecepieFinder;

USE RecepieFinder;

DROP TABLE IF EXISTS RecepieIngredient;
DROP TABLE IF EXISTS UserFavouriteRecepie;
DROP TABLE IF EXISTS Ingredient;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Recepie;
DROP TABLE IF EXISTS User;

CREATE TABLE Category(
	categoryId INT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(20) NOT NULL
);

CREATE TABLE Ingredient(
	ingredientId INT AUTO_INCREMENT PRIMARY KEY,
    ingredientName VARCHAR(30) NOT NULL,
	categoryId INT,
    FOREIGN KEY(categoryId) REFERENCES Category(categoryId)
);

CREATE TABLE Recepie(
	recepieId INT AUTO_INCREMENT PRIMARY KEY,
    recepieName VARCHAR(30) NOT NULL,
    recepieInstructions VARCHAR(2000) NOT NULL
);

CREATE TABLE RecepieIngredient(
	ingredientId INT,
    recepieId INT,
    PRIMARY KEY(ingredientId, recepieId),
    FOREIGN KEY(ingredientId) REFERENCES Ingredient(ingredientId),
    FOREIGN KEY(recepieId) REFERENCES Recepie(recepieId)
);

CREATE TABLE User(
	userId INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(30) NOT NULL,
	userEmail VARCHAR(30) NOT NULL UNIQUE,
	userPassword VARCHAR(100) NOT NULL
);

CREATE TABLE UserFavouriteRecepie(
	userId INT,
    recepieId INT,
    PRIMARY KEY(userId, recepieId),
    FOREIGN KEY(userId) REFERENCES User(userId),
    FOREIGN KEY(recepieId) REFERENCES Recepie(recepieId)
);

-- Inserting demo data into tables

INSERT INTO User(userName, userEmail, userPassword)
VALUES("Zuhair", "zuhairhasan2003@gmail.com", "hello"),
("Hasan", "zuhairhasan123@gmail.com", "hello"),
("Raza", "zuhairhasan@gmail.com", "hello");

INSERT INTO Category(categoryName)
VALUES("essential"),
("non-essential");

INSERT INTO Ingredient(ingredientName, categoryId)			-- 1 = essential,        2 = non-essential
VALUES("salt", 1),
("pepper", 1),
("chicken", 2),
("beef", 2),
("fish", 2),
("potatoe", 2),
("carrot", 2),
("cheese", 2),
("bread", 2),
("egg", 2),
("cooking oil", 1),
("flour", 2),
("water", 1),
("tomatoe", 2);

INSERT INTO Recepie(recepieName, recepieInstructions)
VALUES("fish and chips", "How to cook fish and chips"),
("smash burger", "How to cook smash burger"),
("pizza", "How to cook pizza"),
("grilled cheese sandwich", "How to cook grilled cheese sandwich");

INSERT INTO RecepieIngredient(recepieId, ingredientId)
VALUES((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "fish")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "salt")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "pepper")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "potatoe")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "egg")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "flour")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "cooking oil")),
((select recepieId from Recepie where recepieName = "fish and chips"), (select ingredientId from Ingredient where ingredientName = "water")),
((select recepieId from Recepie where recepieName = "smash burger"), (select ingredientId from Ingredient where ingredientName = "salt")),
((select recepieId from Recepie where recepieName = "smash burger"), (select ingredientId from Ingredient where ingredientName = "pepper")),
((select recepieId from Recepie where recepieName = "smash burger"), (select ingredientId from Ingredient where ingredientName = "beef")),
((select recepieId from Recepie where recepieName = "smash burger"), (select ingredientId from Ingredient where ingredientName = "cheese")),
((select recepieId from Recepie where recepieName = "smash burger"), (select ingredientId from Ingredient where ingredientName = "bread")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "salt")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "pepper")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "chicken")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "cheese")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "cooking oil")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "flour")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "water")),
((select recepieId from Recepie where recepieName = "pizza"), (select ingredientId from Ingredient where ingredientName = "tomatoe")),
((select recepieId from Recepie where recepieName = "grilled cheese sandwich"), (select ingredientId from Ingredient where ingredientName = "cheese")),
((select recepieId from Recepie where recepieName = "grilled cheese sandwich"), (select ingredientId from Ingredient where ingredientName = "bread")),
((select recepieId from Recepie where recepieName = "grilled cheese sandwich"), (select ingredientId from Ingredient where ingredientName = "cooking oil"));

INSERT INTO UserFavouriteRecepie(userId, recepieId)
VALUES ((select userId from User where userName = "zuhair"), (select recepieId from Recepie where recepieName = "grilled cheese sandwich")),
((select userId from User where userName = "zuhair"), (select recepieId from Recepie where recepieName = "pizza"));