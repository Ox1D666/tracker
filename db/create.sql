/*CREATE TABLE IF NOT EXISTS  Roles (
	id SERIAL PRIMARY KEY,
	role text 
);

CREATE TABLE IF NOT EXISTS Users (
    id SERIAL PRIMARY KEY,
    login text,
    password text,
	RoleID int references Roles(id)
);

CREATE TABLE IF NOT EXISTS Rules (
	id SERIAL PRIMARY KEY,
	rule text
);

CREATE TABLE IF NOT EXISTS Role_Rule (
    id SERIAL PRIMARY KEY,
    RoleID INT REFERENCES Roles(id),
    RuleID INT REFERENCES Rules(id)
);

CREATE TABLE IF NOT EXISTS Сategories (
	id SERIAL PRIMARY KEY,
	Сategory text
);

CREATE TABLE IF NOT EXISTS States (
	id SERIAL PRIMARY KEY,
	state text 
);*/

CREATE TABLE IF NOT EXISTS Items (
	id serial primary key,
	name text,
--	UserID int references Users(id),
--	State int references States(id),
--	Category int references Сategories(id)
);

/*CREATE TABLE IF NOT EXISTS Comments (
	id SERIAL PRIMARY KEY,
	Comment text, 
	ItemID int references Items(id)
);

CREATE TABLE IF NOT EXISTS Attachments (
	id SERIAL PRIMARY KEY,
	File text, 
	ItemID int references Items(id)
);

INSERT INTO Roles(id, role) values(1, 'administraror'),
(2, 'initiator'),
(3, 'executor');
INSERT INTO Users(id, login, password, RoleID) values(1, 'admin', 'admin', 1),
(2, 'manager', 'qwerty', 2),
(3, 'worker', '123', 3);*/







