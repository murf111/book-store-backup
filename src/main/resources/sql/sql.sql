------------------------------------------------------------------------------------------------------------------------

-- USERS TABLE (Holds Email, Password, Name, Role for EVERYONE)
-- ALL PASSWORDS are reset to "password" for testing.
-- Hash: $2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe

-- EMPLOYEES
INSERT INTO USERS (id, email, password, name, role) VALUES 
(1, 'john.doe@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'John Doe', 'EMPLOYEE'),
(2, 'jane.smith@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Jane Smith', 'EMPLOYEE'),
(3, 'bob.jones@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Bob Jones', 'EMPLOYEE'),
(4, 'alice.white@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Alice White', 'EMPLOYEE'),
(5, 'mike.wilson@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Mike Wilson', 'EMPLOYEE'),
(6, 'sara.brown@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Sara Brown', 'EMPLOYEE'),
(7, 'tom.jenkins@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Tom Jenkins', 'EMPLOYEE'),
(8, 'lisa.taylor@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Lisa Taylor', 'EMPLOYEE'),
(9, 'david.wright@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'David Wright', 'EMPLOYEE'),
(10, 'emily.harris@email.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Emily Harris', 'EMPLOYEE');

-- CLIENTS
INSERT INTO USERS (id, email, password, name, role) VALUES 
(11, 'client1@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Medelyn Wright', 'CLIENT'),
(12, 'client2@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Landon Phillips', 'CLIENT'),
(13, 'client3@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Harmony Mason', 'CLIENT'),
(14, 'client4@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Archer Harper', 'CLIENT'),
(15, 'client5@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Kira Jacobs', 'CLIENT'),
(16, 'client6@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Maximus Kelly', 'CLIENT'),
(17, 'client7@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Sierra Mitchell', 'CLIENT'),
(18, 'client8@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Quinton Saunders', 'CLIENT'),
(19, 'client9@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Amina Clarke', 'CLIENT'),
(20, 'client10@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Bryson Chavez', 'CLIENT');

------------------------------------------------------------------------------------------------------------------------

-- EMPLOYEES TABLE (Specific Info, linked by ID)

INSERT INTO EMPLOYEES (id, birth_date, phone) VALUES 
(1, '1990-05-15', '555-123-4567'),
(2, '1985-09-20', '555-987-6543'),
(3, '1978-03-08', '555-321-6789'),
(4, '1982-11-25', '555-876-5432'),
(5, '1995-07-12', '555-234-5678'),
(6, '1989-01-30', '555-876-5433'),
(7, '1975-06-18', '555-345-6789'),
(8, '1987-12-04', '555-789-0123'),
(9, '1992-08-22', '555-456-7890'),
(10, '1980-04-10', '555-098-7654');

------------------------------------------------------------------------------------------------------------------------

-- CLIENTS TABLE (Specific Info, linked by ID)
-- Note: User #20 is set to BLOCKED=true for testing

INSERT INTO CLIENTS (id, balance, is_blocked) VALUES 
(11, 1000.00, false),
(12, 1500.50, false),
(13, 800.75, false),
(14, 1200.25, false),
(15, 900.80, false),
(16, 1100.60, false),
(17, 1300.45, false),
(18, 950.30, false),
(19, 1050.90, false),
(20, 880.20, true); 

------------------------------------------------------------------------------------------------------------------------

-- BOOKS TABLE

INSERT INTO BOOKS (name, genre, age_group, price, publication_year, author, number_of_pages, characteristics, description, language)
VALUES 
('The Hidden Treasure', 'Adventure', 'ADULT', 24.99, '2018-05-15', 'Emily White', 400, 'Mysterious journey','An enthralling adventure of discovery', 'ENGLISH'),
('Echoes of Eternity', 'Fantasy', 'TEEN', 16.50, '2011-01-15', 'Daniel Black', 350, 'Magical realms', 'A spellbinding tale of magic and destiny', 'ENGLISH'),
('Whispers in the Shadows', 'Mystery', 'ADULT', 29.95, '2018-08-11', 'Sophia Green', 450, 'Intriguing suspense','A gripping mystery that keeps you guessing', 'ENGLISH'),
('The Starlight Sonata', 'Romance', 'ADULT', 21.75, '2011-05-15', 'Michael Rose', 320, 'Heartwarming love story','A beautiful journey of love and passion', 'ENGLISH'),
('Beyond the Horizon', 'Science Fiction', 'CHILD', 18.99, '2004-05-15', 'Alex Carter', 280,'Interstellar adventure', 'An epic sci-fi adventure beyond the stars', 'ENGLISH'),
('Dancing with Shadows', 'Thriller', 'ADULT', 26.50, '2015-05-15', 'Olivia Smith', 380, 'Suspenseful twists','A thrilling tale of danger and intrigue', 'ENGLISH'),
('Voices in the Wind', 'Historical Fiction', 'ADULT', 32.00, '2017-05-15', 'William Turner', 500,'Rich historical setting', 'A compelling journey through time', 'ENGLISH'),
('Serenade of Souls', 'Fantasy', 'TEEN', 15.99, '2013-05-15', 'Isabella Reed', 330, 'Enchanting realms','A magical fantasy filled with wonder', 'ENGLISH'),
('Silent Whispers', 'Mystery', 'ADULT', 27.50, '2021-05-15', 'Benjamin Hall', 420, 'Intricate detective work','A mystery that keeps you on the edge', 'ENGLISH'),
('Whirlwind Romance', 'Romance', 'OTHER', 23.25, '2022-05-15', 'Emma Turner', 360, 'Passionate love affair','A romance that sweeps you off your feet', 'ENGLISH');

------------------------------------------------------------------------------------------------------------------------

-- DYNAMIC SEQUENCE RESET (Works for H2)
-- Automatically sets the next ID to be (Max ID + 1)
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH (SELECT MAX(ID) + 1 FROM USERS);

-- PostgreSQL Syntax (Do not use this for H2) FOR FUTURE
-- SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));