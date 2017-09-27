-- Test data

-- noinspection SqlResolveForFile

-- Add two users
INSERT INTO users (login, name, password) VALUES ('mark.johnson@yahoo.com', 'Mark Johnson', 'password');
INSERT INTO users (login, name, password) VALUES ('miranda.johnson@yahoo.com', 'Miranda Johnson', 'password');

-- Add devices
INSERT INTO devices (user_id, name) VALUES (1, 'Samsung Galaxy S8');
INSERT INTO devices (user_id, name) VALUES (1, 'Samsung Tab A');
INSERT INTO devices (user_id, name) VALUES (2, 'Apple iPhone S7');
INSERT INTO devices (user_id, name) VALUES (1, 'MacBook Air');

-- Fill first shopping list with goods
INSERT INTO lists (name, description, owner_id) VALUES ('CPUs', 'Processors for servers #1, #6, #11', 1);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Core i5', 'CPU Intel Core i5', 2, 1, 0, 1, 1, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Core i7', 'CPU Intel Core i7', 1, 1, 0, 1, 1, FALSE);

-- Add second list with goods
INSERT INTO lists (name, description, owner_id) VALUES ('HDDs', 'Hard drives for servers #1, #6, #11, #12', 1);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'WD 1TB', 'Western Digital WD Blue Desktop 1 TB (WD10EZRZ)', 1, 2, 0, 1, 1, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (FALSE, 'Seagate 1TB', 'Seagate STEA1000400', 1, 2, 0, 1, 1, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'SSD Kingston 240G', 'Kingston SUV400S37/240G', 1, 2, 0, 1, 1, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'WD 500GB', 'Western Digital WD Black 500 GB (WD5000LPLX)', 2, 2, 0, 1, 1, FALSE);

-- Add 3rd list with goods
INSERT INTO lists (name, description, owner_id) VALUES ('Flowers', 'Flowers for Marta''s birthday', 2);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Rose', 'Red roses', 11, 3, 0, 2, 3, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Chrysanthemum', 'White', 7, 3, 0, 2, 3, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Tulip', 'White', 7, 3, 0, 2, 3, TRUE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Daisy Flowers', 'Green', 3, 3, 0, 2, 3, TRUE);

-- Add 4th list with mix authors
INSERT INTO lists (name, description, owner_id) VALUES ('Gifts', 'Gifts for Marta''s birthday', 2);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Candy', 'Kommunarka', 1, 4, 1, 2, 3, FALSE);
INSERT INTO list_items (checked, name, description, quantity, list_id, version, author_id, device_id, is_deleted)
VALUES (TRUE, 'Champagne', 'Moët & Chandon 2008', 3, 4, 0, 1, 2, FALSE);

-- Add empty lists
INSERT INTO lists (name, description, owner_id) VALUES ('RAMs', 'Memory for servers', 1);
INSERT INTO lists (name, description, owner_id) VALUES ('New List', '', 2);
