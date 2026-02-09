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
(20, 'client10@example.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Bryson Chavez', 'CLIENT'),
(21, 'suchdenys220@gmail.com', '$2a$10$hNUcmFGKIBG49pmL0ftauO1q3Y.QaMXEwfpvMLRCNGgZp.90o5MSe', 'Denys Sych', 'CLIENT');

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
(20, 880.20, true),
(21, 1000, false);

------------------------------------------------------------------------------------------------------------------------

-- BOOKS TABLE

INSERT INTO BOOKS (name, genre, age_group, price, publication_year, author, number_of_pages, characteristics, description, language, image_url)
VALUES 
('Harry Potter and the Philosopher''s Stone', 'Fantasy', 'TEEN', 19.99, '1997-06-26', 'J.K. Rowling', 223, 'Hardcover, Magic', 'A young wizard discovers his magical heritage on his eleventh birthday.', 'ENGLISH', 'book1.jpg'),
('1984', 'Science Fiction', 'ADULT', 14.50, '1949-06-08', 'George Orwell', 328, 'Dystopian, Political', 'A chilling prophecy about the future of a totalitarian regime.', 'ENGLISH', 'book2.jpg'),
('The Great Gatsby', 'Historical Fiction', 'ADULT', 12.99, '1925-04-10', 'F. Scott Fitzgerald', 180, 'Classic, Jazz Age', 'The story of the fabulously wealthy Jay Gatsby and his love for Daisy Buchanan.', 'ENGLISH', 'book3.jpg'),
('Dune', 'Science Fiction', 'ADULT', 24.95, '1965-08-01', 'Frank Herbert', 412, 'Epic, Space Opera', 'Set on the desert planet Arrakis, it is the story of the boy Paul Atreides.', 'ENGLISH', 'book4.jpg'),
('Pride and Prejudice', 'Romance', 'ADULT', 9.99, '1813-01-28', 'Jane Austen', 279, 'Classic, Regency', 'A romantic novel of manners that depicts the British gentry.', 'ENGLISH', 'book5.jpg'),
('The Hobbit', 'Fantasy', 'CHILD', 15.00, '1937-09-21', 'J.R.R. Tolkien', 310, 'Adventure, Middle-earth', 'Bilbo Baggins, a hobbit, journeys to the Lonely Mountain to reclaim treasure.', 'ENGLISH', 'book6.jpg'),
('The Da Vinci Code', 'Thriller', 'ADULT', 18.00, '2003-03-18', 'Dan Brown', 489, 'Mystery, Conspiracy', 'A symbologist races to uncover a religious mystery protected by a secret society.', 'ENGLISH', 'book7.jpg'),
('Murder on the Orient Express', 'Mystery', 'ADULT', 13.50, '1934-01-01', 'Agatha Christie', 256, 'Detective, Crime', 'Hercule Poirot investigates a murder on a luxurious train stopped by snow.', 'ENGLISH', 'book8.jpg'),
('Charlotte''s Web', 'Adventure', 'CHILD', 8.99, '1952-10-15', 'E.B. White', 192, 'Illustrated, Animals', 'The story of a pig named Wilbur and his friendship with a barn spider named Charlotte.', 'ENGLISH', 'book9.jpg'),
('The Shining', 'Thriller', 'ADULT', 21.00, '1977-01-28', 'Stephen King', 447, 'Horror, Psychological', 'Jack Torrance takes a job as a caretaker of the isolated Overlook Hotel.', 'ENGLISH', 'book10.jpg'),
('Kobzar', 'Historical Fiction', 'ADULT', 25.00, '1840-04-18', 'Taras Shevchenko', 115, 'Poetry, Classic', 'A collection of poems that is a monument of Ukrainian literature.', 'UKRAINIAN', 'book11.jpg'),
('Shadows of Forgotten Ancestors', 'Historical Fiction', 'ADULT', 11.50, '1911-01-01', 'Mykhailo Kotsiubynsky', 200, 'Folklore, Classic', 'A tragic love story set among the Hutsul people of the Carpathian Mountains.', 'UKRAINIAN', 'book12.jpg');
------------------------------------------------------------------------------------------------------------------------

-- DYNAMIC SEQUENCE RESET (Works for H2)
-- Automatically sets the next ID to be (Max ID + 1)
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH (SELECT MAX(ID) + 1 FROM USERS);

-- PostgreSQL Syntax (Do not use this for H2) FOR FUTURE
-- SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));