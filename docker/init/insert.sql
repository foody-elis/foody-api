-- README: Passwords are hashed using BCrypt. The password for all users is 'password'.

-- Insert admin
INSERT INTO users (created_at, email, password, name, surname, birth_date, phone_number, role, active)
VALUES (now(), 'admin@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Admin', 'User', '1980-01-01', '+123456789', 'ADMIN', true);

-- Insert customers
INSERT INTO users (created_at, email, password, name, surname, birth_date, phone_number, role, active)
VALUES
    (now(), 'mario.rossi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Mario', 'Rossi', '1990-05-15', '+393331234568', 'CUSTOMER', true),
    (now(), 'luigi.verdi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Luigi', 'Verdi', '1987-09-22', '+393331234569', 'CUSTOMER', true),
    (now(), 'anna.bianchi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Anna', 'Bianchi', '1995-03-08', '+393331234570', 'CUSTOMER', true),
    (now(), 'carla.neri@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Carla', 'Neri', '1993-10-05', '+393331234585', 'CUSTOMER', true),
    (now(), 'giovanni.rossi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Giovanni', 'Rossi', '1991-12-15', '+393331234586', 'CUSTOMER', true);

-- Insert restaurateurs
INSERT INTO users (created_at, email, password, name, surname, birth_date, phone_number, role, active)
VALUES
    (now(), 'paolo.galli@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Paolo', 'Galli', '1980-12-10', '+393331234571', 'RESTAURATEUR', true),
    (now(), 'giulia.neri@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Giulia', 'Neri', '1978-11-18', '+393331234572', 'RESTAURATEUR', true),
    (now(), 'andrea.marchi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Andrea', 'Marchi', '1985-04-22', '+393331234581', 'RESTAURATEUR', true),
    (now(), 'silvia.valli@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Silvia', 'Valli', '1990-08-19', '+393331234582', 'RESTAURATEUR', true);

-- Insert addresses
INSERT INTO addresses (created_at, street, city, civic_number, postal_code, province)
VALUES
    (now(), 'Via Garibaldi', 'Roma', '12', '00185', 'RM'),
    (now(), 'Corso Vittorio Emanuele', 'Milano', '5', '20121', 'MI'),
    (now(), 'Piazza Dante', 'Napoli', '3', '80134', 'NA'),
    (now(), 'Via delle Alpi', 'Torino', '8', '10141', 'TO');

-- Insert categories
INSERT INTO categories (name)
VALUES
    ('Italiana'),
    ('Cinese'),
    ('Messicana'),
    ('Giapponese'),
    ('Americana'),
    ('Francese'),
    ('Vegetariana'),
    ('Vegan'),
    ('Fusion');

-- Insert restaurants
INSERT INTO restaurants (created_at, name, description, phone_number, seats, approved, address_id, restaurateur_id)
VALUES
    (now(), 'Trattoria da Mario', 'Specialità romane e atmosfera familiare', '+393331234573', 40, true, 1, 7),
    (now(), 'Sushi Milano', 'Autentica cucina giapponese con un tocco moderno', '+393331234574', 30, true, 2, 8),
    (now(), 'Osteria Partenopea', 'Cucina napoletana tradizionale', '+393331234575', 50, true, 3, 9),
    (now(), 'Ristorante delle Alpi', 'Piatti tipici piemontesi e vini locali', '+393331234576', 45, true, 4, 10);

-- Associate restaurants with categories
INSERT INTO restaurant_category (restaurant_id, category_id)
VALUES
    (1, 1),
    (1, 7),
    (2, 4),
    (2, 9),
    (3, 1),
    (3, 8),
    (4, 1),
    (4, 5);

-- Insert waiters and cooks for Restaurant with id = 1
INSERT INTO users (created_at, email, password, name, surname, birth_date, phone_number, role, active, employer_restaurant_id)
VALUES
    (now(), 'giovanni.ferri@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Giovanni', 'Ferri', '1992-06-14', '+393331234577', 'WAITER', true, 1),
    (now(), 'marco.bianchi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Marco', 'Bianchi', '1990-04-25', '+393331234588', 'WAITER', true, 1),
    (now(), 'fabio.carra@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Fabio', 'Carra', '1988-03-20', '+393331234579', 'COOK', true, 1),
    (now(), 'giorgio.verdi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Giorgio', 'Verdi', '1985-09-14', '+393331234590', 'COOK', true, 1);

-- Insert waiters and cooks for Restaurant with id = 2
INSERT INTO users (created_at, email, password, name, surname, birth_date, phone_number, role, active, employer_restaurant_id)
VALUES
    (now(), 'marta.longo@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Marta', 'Longo', '1994-01-12', '+393331234578', 'WAITER', true, 2),
    (now(), 'luca.rossi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Luca', 'Rossi', '1991-11-30', '+393331234589', 'WAITER', true, 2),
    (now(), 'alessandra.marchi@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Alessandra', 'Marchi', '1990-07-25', '+393331234580', 'COOK', true, 2),
    (now(), 'simone.neri@example.com', '$2a$10$vaiHi0/bxVLfv5eGuGPk6OCXBr/NE3HF3wZmItVzeFU0L0ojnjMTe', 'Simone', 'Neri', '1989-02-18', '+393331234591', 'COOK', true, 2);

-- Insert week day infos for Restaurant with id = 1
INSERT INTO week_day_infos (created_at, week_day, start_launch, end_launch, start_dinner, end_dinner, sitting_time_step, restaurant_id)
VALUES
    (now(), 1, '12:00:00', '14:00:00', '19:00:00', '21:00:00', 'THIRTY', 1),
    (now(), 2, '12:00:00', '14:00:00', '19:00:00', '21:00:00', 'THIRTY', 1),
    (now(), 3, NULL, NULL, '19:00:00', '21:00:00', 'THIRTY', 1);

-- Insert week day infos for Restaurant with id = 2
INSERT INTO week_day_infos (created_at, week_day, start_launch, end_launch, start_dinner, end_dinner, sitting_time_step, restaurant_id)
VALUES
    (now(), 1, '12:00:00', '14:00:00', '19:00:00', '21:00:00', 'THIRTY', 2),
    (now(), 2, '12:00:00', '14:00:00', '19:00:00', '21:00:00', 'THIRTY', 2),
    (now(), 3, NULL, NULL, '19:00:00', '21:00:00', 'THIRTY', 2);

-- Insert sitting times for WeekDayInfo of Restaurant with id = 1
INSERT INTO sitting_times (created_at, start, end, week_day_info_id)
VALUES
    (now(), '12:00:00', '12:30:00', 1),
    (now(), '12:30:00', '13:00:00', 1),
    (now(), '13:00:00', '13:30:00', 1),
    (now(), '13:30:00', '14:00:00', 1),
    (now(), '19:00:00', '19:30:00', 1),
    (now(), '19:30:00', '20:00:00', 1),
    (now(), '20:00:00', '20:30:00', 1),
    (now(), '20:30:00', '21:00:00', 1),
    (now(), '12:00:00', '12:30:00', 2),
    (now(), '12:30:00', '13:00:00', 2),
    (now(), '13:00:00', '13:30:00', 2),
    (now(), '13:30:00', '14:00:00', 2),
    (now(), '19:00:00', '19:30:00', 2),
    (now(), '19:30:00', '20:00:00', 2),
    (now(), '20:00:00', '20:30:00', 2),
    (now(), '20:30:00', '21:00:00', 2),
    (now(), '19:00:00', '19:30:00', 3),
    (now(), '19:30:00', '20:00:00', 3),
    (now(), '20:00:00', '20:30:00', 3),
    (now(), '20:30:00', '21:00:00', 3);

-- Insert sitting times for WeekDayInfo of Restaurant with id = 2
INSERT INTO sitting_times (created_at, end, start, week_day_info_id)
VALUES
    (now(), '12:00:00', '12:30:00', 4),
    (now(), '12:30:00', '13:00:00', 4),
    (now(), '13:00:00', '13:30:00', 4),
    (now(), '13:30:00', '14:00:00', 4),
    (now(), '19:00:00', '19:30:00', 4),
    (now(), '19:30:00', '20:00:00', 4),
    (now(), '20:00:00', '20:30:00', 4),
    (now(), '20:30:00', '21:00:00', 4),
    (now(), '12:00:00', '12:30:00', 5),
    (now(), '12:30:00', '13:00:00', 5),
    (now(), '13:00:00', '13:30:00', 5),
    (now(), '13:30:00', '14:00:00', 5),
    (now(), '19:00:00', '19:30:00', 5),
    (now(), '19:30:00', '20:00:00', 5),
    (now(), '20:00:00', '20:30:00', 5),
    (now(), '20:30:00', '21:00:00', 5),
    (now(), '19:00:00', '19:30:00', 6),
    (now(), '19:30:00', '20:00:00', 6),
    (now(), '20:00:00', '20:30:00', 6),
    (now(), '20:30:00', '21:00:00', 6);

-- Insert dishes for Restaurant with id = 1
INSERT INTO dishes (created_at, name, description, price, restaurant_id)
VALUES
    (now(), 'Carbonara', 'Pasta con guanciale, uova, pecorino e pepe', 12.00, 1),
    (now(), 'Amatriciana', 'Pasta con guanciale, pomodoro e pecorino', 11.50, 1),
    (now(), 'Sushi Misto', 'Assortimento di nigiri, sashimi e roll', 25.00, 1),
    (now(), 'Ramen di Maiale', 'Brodo ricco con noodles, maiale e uovo', 15.00, 1),
    (now(), 'Pizza Margherita', 'Pizza con pomodoro, mozzarella e basilico', 8.50, 1),
    (now(), 'Scialatielli ai Frutti di Mare', 'Pasta fatta in casa con frutti di mare freschi', 18.00, 1);

-- Insert dishes for Restaurant with id = 2
INSERT INTO dishes (created_at, name, description, price, restaurant_id)
VALUES
    (now(), 'Bagna Cauda', 'Specialità piemontese a base di acciughe e aglio', 10.00, 2),
    (now(), 'Brasato al Barolo', 'Carne di manzo cotta lentamente nel vino Barolo', 22.00, 2),
    (now(), 'Bistecca alla Fiorentina', 'Taglio di carne toscano servito al sangue', 35.00, 2),
    (now(), 'Pappa al Pomodoro', 'Zuppa di pomodoro tradizionale toscana', 9.50, 2),
    (now(), 'Risotto al Nero di Seppia', 'Risotto cremoso con nero di seppia', 16.00, 2),
    (now(), 'Sarde in Saor', 'Sardine marinate con cipolle e aceto', 12.00, 2);

-- Insert bookings for different customers and sitting times
INSERT INTO bookings (created_at, date, seats, status, customer_id, restaurant_id, sitting_time_id)
VALUES
    (now(), '2025-11-01', 2, 'ACTIVE', 2, 1, 1),
    (now(), '2025-11-01', 3, 'ACTIVE', 3, 1, 2),
    (now(), '2025-11-01', 4, 'ACTIVE', 3, 1, 4),
    (now(), '2025-11-01', 2, 'ACTIVE', 2, 1, 5),
    (now(), '2025-11-01', 3, 'ACTIVE', 2, 1, 6),
    (now(), '2024-10-22', 3, 'ACTIVE', 4, 1, 1),
    (now(), '2024-10-06', 2, 'ACTIVE', 4, 2, 21),
    (now(), '2024-10-07', 4, 'ACTIVE', 4, 2, 29),
    (now(), '2024-10-08', 2, 'ACTIVE', 4, 2, 37);

-- Insert reviews for different customers and dishes
INSERT INTO reviews (created_at, title, description, rating, customer_id, dish_id, restaurant_id)
VALUES
    (now(), 'Eccellente', 'Un piatto davvero straordinario!', 5, 2, 1, 1),
    (now(), 'Molto buono', 'Esperienza piacevole e piatti ben curati.', 4, 2, 2, 2),
    (now(), 'Soddisfacente', 'Un buon rapporto qualità-prezzo.', 3, 3, 3, 3),
    (now(), 'Non male', 'Servizio rapido, ma piatti migliorabili.', 3, 3, 4, 1),
    (now(), 'Indimenticabile', 'Atmosfera e cibo perfetti!', 5, 2, NULL, 2),
    (now(), 'Piacevole esperienza', 'Un ristorante da consigliare.', 4, 3, NULL, 2),
    (now(), 'Servizio ottimo', 'Camerieri gentili e cucina eccellente.', 5, 4, NULL, 1),
    (now(), 'Eccellente', 'I Scialatielli erano incredibili!', 5, 2, 6, 1),
    (now(), 'Un sapore unico', 'La Bagna Cauda mi ha sorpreso.', 4, 3, 7, 2),
    (now(), 'Buonissima', 'Una Carbonara eseguita alla perfezione.', 5, 4, 1, 1);

-- Insert orders for customers with active bookings
INSERT INTO orders (created_at, table_code, buyer_id, restaurant_id, status)
VALUES
     (now(), 'TBL001', 2, 1, 'PREPARING'),
     (now(), 'TBL002', 3, 1, 'PREPARING'),
     (now(), 'TBL003', 3, 1, 'PREPARING'),
     (now(), 'TBL004', 2, 1, 'PREPARING'),
     (now(), 'TBL005', 2, 1, 'PREPARING'),
     (now(), 'TBL006', 2, 2, 'COMPLETED'),
     (now(), 'TBL007', 3, 2, 'COMPLETED'),
     (now(), 'TBL008', 4, 1, 'COMPLETED');

-- Associate orders with dishes
INSERT INTO order_dish (order_id, dish_id, quantity)
VALUES
     (1, 1, 2),
     (1, 2, 1),
     (2, 3, 3),
     (2, 4, 1),
     (3, 2, 2),
     (3, 5, 2),
     (4, 3, 1),
     (4, 4, 1),
     (5, 1, 3),
     (5, 5, 1),
     (6, 6, 2),
     (7, 7, 1),
     (8, 1, 2);