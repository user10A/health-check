INSERT INTO user_account (id, email, password, role)
VALUES
    (1, 'mia.brown@gmail.com', '$2a$10$R09T1ozvLWYvh4A6pFhWe.PsSxnoo2A.MXfD1yROEgSWr8rQlye5G()', 'ADMIN'),
    (2, 'john.doe@gmail.com', '$2a$10$NpPdEn2CswW4aj3gqHfTveqg42GR/kNjM.UxsZp6CFjt.mR3Dc0Lm', 'USER'),
    (3, 'alice.smith@gmail.com', '$2a$10$GSyQFd8GLOlVfzUzk7ouYuf2Hq1TGZt5ac7rOMXt2AoBb/ObRov8G', 'USER'),
    (4, 'charlie.brown@gmail.com', '$2a$10$BP2LeQU2.sYh7toztf3l5eFp9AnPV/uDDrnFMvSXoyJIHlcw6mUlq', 'USER'),
    (5, 'emma.jones@gmail.com', '$2a$10$9FTYj.3jiDJVoqSWh4eF7.3QTXpFlr7h7uqrTKtYSPsHk1htYz0Nu', 'USER'),
    (6, 'michael.wilson@gmail.com', '$2a$10$eXGTIeu1/HUnfJ3U7JWt5O21Q/fIR7FzDfzVHrU6yMa8Te.FW.4kK', 'USER'),
    (7, 'sarah.miller@gmail.com', '$2a$10$yN.EQPkh/YUfAyFciHgxFeY0YOfdXJkiTfHH9vC4d3GnOCZOq7o1i', 'USER'),
    (8, 'peter.clark@gmail.com', '$2a$10$gByc5cfM5wqpy.dqTNSv6OH1yZqj2Q1e0z7x92aMBmkV1RvlPK.ia', 'USER'),
    (9, 'linda.white@gmail.com', '$2a$10$n3ZcCHQup3W/HHte66l5v.qylqeXCMz3MnhZnZq.v3N0m1VnnV/qe', 'USER'),
    (10, 'ryan.jackson@gmail.com', '$2a$10$.SUQZ18tJSrcDLS9aRy6uOMNrWYHVOq/FJkUJg/MN8.NIt.S8G6bm', 'USER'),
    (11, 'olivia.wilson@gmail.com', '$2a$10$UFlF9hR5T6eBlT.LZFPcnOSun8Yt/wLNZOBXo/WG/QlFvJk78z0Hm', 'USER'),
    (12, 'david.thomas@gmail.com', '$2a$10$AF44xmM3gs5XlOxEruyupOqlQx2ZNLlqMBH9f26Q0KuMa.4Fbd1lG', 'USER'),
    (13, 'emily.jones@gmail.com', '$2a$10$Z7gTgStAlvXwCZLFpUTCTOAhvXGEfqJGmWsC.I4ySLEpW8Vhz4NKG', 'USER'),
    (14, 'james.baker@gmail.com', '$2a$10$YsgjMN5EwXYPeLoiSkayluwudufD2XiU/YSaStnb.N5ioC9zFlO7K', 'USER'),
    (15, 'hannah.green@gmail.com', '$2a$10$1d3hew6XgJFj7WBTfyLPi.yWpyWmlr3U9JSds9pS5hxQ/l1lUp9/K', 'USER'),
    (16, 'nathan.morris@gmail.com', '$2a$10$Z.Ip7YQpnujJfjDwz1y6YeCzmS/KZPpji.g1v9B4HzUgAeLBw1hP6', 'USER'),
    (17, 'ella.martin@gmail.com', '$2a$10$uxXoKWha89/T8F3TJ40FZuWnRYVXwZIQdWZtGoc2tluFWOzE9x2c6', 'USER'),
    (18, 'adam.white@gmail.com', '$2a$10$EMvfTbSgPyLFptbC8Sve4O.9J4QErn1jbjK4v6Z7eU8RpLjQaN/Zm', 'USER'),
    (19, 'grace.thompson@gmail.com', '$2a$10$FqFbkj58qBpZmATXKuXc.uWQuHx4ZTrJI4Xw8ckdKo7e1wAFZfbD6', 'USER'),
    (20, 'jason.miller@gmail.com', '$2a$10$6BXv2.yH.ZqkTW3T4Kmmn.o0gQ.ZhZtOoy5h9ZSfbJKs6n6VUSl9C', 'USER');
--  Password=Pa$$w0rd3
INSERT INTO users (id, first_name, last_name, phone_number, user_account_id)
VALUES
    (1, 'Mia', 'Brown', '+9961234567890', 1),
    (2, 'John', 'Doe', '+9969876543210', 2),
    (3, 'Alice', 'Smith', '+9961111111112', 3),
    (4, 'Charlie', 'Brown', '+9962222222223', 4),
    (5, 'Emma', 'Jones', '+9963333333334', 5),
    (6, 'Michael', 'Wilson', '+9964444444445', 6),
    (7, 'Sarah', 'Miller', '+9965555555556', 7),
    (8, 'Peter', 'Clark', '+9966666666667', 8),
    (9, 'Linda', 'White', '+9967777777778', 9),
    (10, 'Ryan', 'Jackson', '+9968888888889', 10),
    (11, 'Olivia', 'Wilson', '+9969999999990', 11),
    (12, 'David', 'Thomas', '+9961010101011', 12),
    (13, 'Emily', 'Jones', '+9961212121212', 13),
    (14, 'James', 'Baker', '+9961313131313', 14),
    (15, 'Hannah', 'Green', '+9961414141414', 15),
    (16, 'Nathan', 'Morris', '+9961515151515', 16),
    (17, 'Ella', 'Martin', '+9961616161616', 17),
    (18, 'Adam', 'White', '+9961717171717', 18),
    (19, 'Grace', 'Thompson', '+9961818181818', 19),
    (20, 'Jason', 'Miller', '+9961919191919', 20);
INSERT INTO application (id, username, date_of_application_creation, phone_number, processed)
VALUES
    (1, 'Sophie Williams', '2024-02-04', '+9962020202020', false),
    (2, 'Daniel Brown', '2024-02-05', '+9962121212121', true),
    (3, 'Ava Johnson', '2024-02-06', '+9962222222222', false),
    (4, 'Jack Davis', '2024-02-07', '+9962323232323', false),
    (5, 'Grace Roberts', '2024-02-08', '+9962424242424', true),
    (6, 'Ethan Taylor', '2024-02-09', '+9962525252525', false),
    (7, 'Sophia Moore', '2024-02-10', '+9962626262626', true),
    (8, 'Mason Anderson', '2024-02-11', '+9962727272727', false),
    (9, 'Chloe Thomas', '2024-02-12', '+9962828282828', false),
    (10, 'William Hill', '2024-02-13', '+9962929292929', false),
    (11, 'Emma Davis', '2024-02-14', '+9963030303030', false),
    (12, 'Aiden Wright', '2024-02-15', '+9963131313131', true),
    (13, 'Ella Jones', '2024-02-16', '+9963232323232', false),
    (14, 'Logan Carter', '2024-02-17', '+9963333333333', true),
    (15, 'Avery Harrison', '2024-02-18', '+9963434343434', false),
    (16, 'Henry Evans', '2024-02-19', '+9963535353535', false),
    (17, 'Zoe Parker', '2024-02-20', '+9963636363636', false),
    (18, 'Owen Cooper', '2024-02-21', '+9963737373737', true),
    (19, 'Mila Bryant', '2024-02-22', '+9963838383838', false),
    (20, 'Jackson Fisher', '2024-02-23', '+9963939393939', false),
    (21, 'Sophie White', '2024-02-24', '+9964040404040', false),
    (22, 'Lucas Reed', '2024-02-25', '+9964141414141', false),
    (23, 'Aria Bell', '2024-02-26', '+9964242424242', false),
    (24, 'Liam Parker', '2024-02-27', '+9964343434343', false),
    (25, 'Scarlett Fisher', '2024-02-28', '+9964444444444', false),
    (26, 'Carter Davis', '2024-02-29', '+9964545454545', false),
    (27, 'Madison White', '2024-03-01', '+9964646464646', false),
    (28, 'Jackson Evans', '2024-03-02', '+9964747474747', false),
    (29, 'Ava Bryant', '2024-03-03', '+9964848484848', false),
    (30, 'Noah Reed', '2024-03-04', '+9964949494949', false),
    (31, 'Isabella Taylor', '2024-03-05', '+9965050505050', false),
    (32, 'Mason Bell', '2024-03-06', '+9965151515151', false),
    (33, 'Aria Parker', '2024-03-07', '+9965252525252', true),
    (34, 'Liam White', '2024-03-08', '+9965353535353', false),
    (35, 'Ella Hill', '2024-03-09', '+9965454545454', false),
    (36, 'Carter Roberts', '2024-03-10', '+9965555555555', false),
    (37, 'Madison Thomas', '2024-03-11', '+9965656565656', false),
    (38, 'Jackson Davis', '2024-03-12', '+9965757575757', false),
    (39, 'Zoe Cooper', '2024-03-13', '+9965858585858', false),
    (40, 'Owen Fisher', '2024-03-14', '+9965959595959', false);
INSERT INTO department (id, facility)
VALUES(1, 'Анестезиология'),
      (2, 'Онкология'),
      (3, 'Терапия'),
      (4, 'Ортопедия'),
      (5, 'Урология'),
      (6, 'Вакцинация'),
      (7, 'Оториноларингология'),
      (8, 'Флебология'),
      (9, 'Гинекология'),
      (10, 'Офтальмология'),
      (11, 'Эндокринология'),
      (12, 'Дерматология'),
      (13, 'Проктология'),
      (14, 'Физиотерапия'),
      (15, 'Кардиология'),
      (16, 'Психотерапия'),
      (17, 'Невропатия'),
      (18, 'Пульмонопатия'),
      (19, 'Ревмотопатия'),
      (20, 'Нейрохирургия');

INSERT INTO doctor (id, first_name, last_name, image, position, description, is_active,  department_id)
VALUES(1, 'Logan', 'Wells', 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUTERIQFRAXFRgWFhUYFRns.','Dermatologist', 'Expert in skin-related conditions and treatments.', true,  1),
      (2, 'Addison', 'Howard', 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD','Dermatologist', 'Expert in skin-related conditions and treatments.', true,  2),
      (3, 'Michael', 'Smith', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Dermatologist', 'Expert in skin-related conditions and treatments.', true,  3),
      (4, 'Emily', 'Johnson', 'data:image/jpeg;base64,/9j/4AoS+ktTPY8CBEi0mZEiQDkGEI0g0oG0Ewh2EE0ALiCYQ7QTiArUEXdY04gXEBKosQrihpp4tb31v7yVFLE+AOiaIqq3M3YqD.','Dermatologist', 'Expert in skin-related conditions and treatments.', true,  4),
      (5, 'Christopher', 'Brown', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Pediatrician', 'Dedicated to childrens health and well-being', true,  5),
      (6, 'William', 'Taylor', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Urologist', 'Specialized in urinary tract disorders and surgeries.', true,  6),
      (7, 'Olivia', 'Davis', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Ophthalmologist', 'Expert in eye care and vision-related issues.', true,  7),
      (8, 'Daniel', 'Anderson', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Endocrinologist', 'Focus on hormonal disorders and endocrine system health.', true,  8),
      (9, 'Emma', 'White', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Rheumatologist', 'Specialized in autoimmune and musculoskeletal disorders.', true,  9),
      (10, 'Alexander', 'Martinez', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'ENT Specialist', 'Expert in ear, nose, and throat conditions and surgeries.', true,  10),
      (11, 'Sophie', 'Moore', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Psychiatrist', 'Dedicated to mental health and psychological well-being.', true,  11),
      (12, 'James', 'Harris', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Gynecologist', 'Specialized in women\s reproductive health.', true,  12),
      (13, 'Grace', 'Nelson', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Pulmonologist', 'Focus on respiratory system disorders and treatments.', true, 13),
      (14, 'Andrew', 'Carter', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Allergist', 'Expert in allergies and immunological disorders.', true, 14),
      (15, 'Mia', 'Cooper', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Geriatrician', 'Dedicated to the health and well-being of the elderly.', true,  15),
      (16, 'Liam', 'Robinson', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Sports Medicine Specialist', 'Expert in sports-related injuries and treatments.', true, 16),
      (17, 'Aria', 'Hill', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Infectious Disease Specialist', 'Focus on infectious diseases and their treatments.', true, 17),
      (18, 'Henry', 'Wright', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Nephrologist', 'Specialized in kidney-related disorders and treatments.', true, 18),
      (19, 'Ella', 'Fisher', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Plastic Surgeon', 'Expert in cosmetic and reconstructive plastic surgery.', true,  19),
      (20, 'Benjamin', 'Lopez', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Hematologist', 'Focus on blood disorders and related treatments.', true, 20),
      (21, 'Lily', 'Baker', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Interventional Radiologist', 'Specialized in minimally invasive radiological procedures.', true,  1),
      (22, 'William', 'Gomez', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Invasive Cardiologist', 'Expert in advanced cardiac interventions.', true, 2),
      (23, 'Zoe', 'Perez', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Pain Management Specialist', 'Focus on managing chronic pain conditions.', true,  3),
      (24, 'Samuel', 'Russell', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Sleep Medicine Specialist', 'Dedicated to diagnosing and treating sleep disorders.', true,  4),
      (25, 'Victoria', 'Foster', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQq30eWo_CwBXLL18h0IboRqFcKnOcQ9g_JinigHj9L2ErhMt_m_9VIgCuimVgLCE7qfI8&usqp=CAU', 'Podiatrist', 'Expert in foot and ankle conditions and surgeries.', true,  5);
INSERT INTO schedule (id, start_date_work, end_date_work, start_day_time, end_day_time, start_break_time, end_break_time, interval_in_minutes, department_id,doctor_id)
VALUES(1, '2024-01-15', '2024-02-15', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FIFTEEN', 1, 1),
      (2, '2024-01-16', '2024-02-16', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FIFTEEN', 2, 2),
      (3, '2024-01-17', '2024-01-17', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FIFTEEN', 3, 3),
      (4, '2024-01-18', '2024-02-18', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FIFTEEN', 4, 4),
      (5, '2024-01-19', '2024-02-19', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FIFTEEN', 5, 5),
      (6, '2024-01-19', '2024-02-20', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 6, 6),
      (7, '2024-01-19', '2024-02-21', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 7, 7),
      (8, '2024-01-22', '2024-02-22', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 8, 8),
      (9, '2024-01-23', '2024-02-23', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 9, 9),
      (10, '2024-01-24', '2024-02-24', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 10, 10),
      (11, '2024-01-25', '2024-02-25', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 11, 11),
      (12, '2024-01-26', '2024-02-26', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 12, 12),
      (13, '2024-01-25', '2024-02-27', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 13, 13),
      (14, '2024-01-25', '2024-02-28', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 14, 14),
      (15, '2024-01-29', '2024-02-29', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 15, 15),
      (16, '2024-01-30', '2024-02-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 16, 16),
      (17, '2024-01-31', '2024-03-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 17, 17),
      (18, '2024-02-01', '2024-03-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 18 ,18),
      (19, '2024-02-02', '2024-03-02', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 19 ,19),
      (20, '2024-02-01', '2024-03-03', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 20 ,20),
      (21, '2024-02-01', '2024-03-04', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 1, 21),
      (22, '2024-02-05', '2024-03-05', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 2, 22),
      (23, '2024-02-06', '2024-03-06', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 3, 23),
      (24, '2024-02-07', '2024-03-07', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 4, 24),
      (25, '2024-02-08', '2024-03-08', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 5 ,25);
INSERT INTO appointment (id, appointment_date, appointment_time, status, processed, user_id, department_id, doctor_id)
VALUES(1, '2024-01-15', '10:00:00', 'CANCELLED', false, 1, 1, 1),
      (2, '2024-01-16', '11:30:00', 'CONFIRMED', false, 2, 2, 2),
      (3, '2024-01-17', '14:15:00', 'CONFIRMED', false, 3, 3, 3),
      (4, '2024-01-18', '09:45:00', 'FINISHED', true, 4, 4, 4),
      (5, '2024-01-19', '16:30:00', 'CANCELLED', false, 5, 5, 5),
      (6, '2024-01-19', '13:00:00', 'CONFIRMED', false, 6, 6, 6),
      (7, '2024-01-19', '10:45:00', 'CANCELLED', false, 7, 7, 7),
      (8, '2024-01-19', '10:30:00', 'CONFIRMED', false, 8, 8, 8),
      (9, '2024-01-23', '15:15:00', 'FINISHED', true, 9, 9, 9),
      (10, '2024-01-24', '08:30:00', 'CONFIRMED', false, 10, 10, 10),
      (11, '2024-01-25', '11:00:00', 'CANCELLED', false, 11, 11, 11),
      (12, '2024-01-26', '14:45:00', 'CONFIRMED', false, 12, 12, 12),
      (13, '2024-01-27', '09:15:00', 'FINISHED', true, 13, 13, 13),
      (14, '2024-01-28', '16:00:00', 'CONFIRMED', false, 14, 14, 14),
      (15, '2024-01-29', '10:30:00', 'CANCELLED', false, 15, 15, 15),
      (16, '2024-01-30', '13:15:00', 'CONFIRMED', false, 16, 16, 16),
      (17, '2024-01-31', '14:30:00', 'FINISHED', true, 17, 17, 17),
      (18, '2024-02-01', '08:45:00', 'CONFIRMED', false, 18, 18, 18),
      (19, '2024-02-02', '11:15:00', 'CANCELLED', false, 19, 19, 19),
      (20, '2024-02-01', '15:45:00', 'CONFIRMED', false, 20, 20, 20);
INSERT INTO result (id, result_date, time_of_uploading_result, pdf_url, result_number, department_id, user_id)
VALUES(1, '2024-01-15', '10:30:00', '/path/to/result.pdf', 12345, 1, 1),
      (2, '2024-01-16', '12:00:00', '/path/to/result_cardiology.pdf', 54321, 2, 2),
      (3, '2024-01-17', '14:45:00', '/path/to/result_dermatology.pdf', 67890, 3, 3),
      (4, '2024-01-18', '11:15:00', '/path/to/result_orthopedics.pdf', 98765, 4, 4),
      (5, '2024-01-19', '16:45:00', '/path/to/result_pediatrics.pdf', 56789, 5, 5),
      (6, '2024-01-19', '13:30:00', '/path/to/result_urology.pdf', 43210, 6, 6),
      (7, '2024-01-19', '11:00:00', '/path/to/result_ophthalmology.pdf', 10987, 7, 7),
      (8, '2024-01-22', '13:45:00', '/path/to/result_endocrinology.pdf', 67890, 8, 8),
      (9, '2024-01-23', '15:30:00', '/path/to/result_rheumatology.pdf', 34567, 9, 9),
      (10, '2024-01-24', '08:45:00', '/path/to/result_ent.pdf', 87654, 10, 10),
      (11, '2024-01-25', '11:15:00', '/path/to/result_psychiatry.pdf', 23456, 11, 11),
      (12, '2024-01-26', '15:00:00', '/path/to/result_gynecology.pdf', 87654, 12, 12),
      (13, '2024-01-25', '09:30:00', '/path/to/result_pulmonology.pdf', 12345, 13, 13),
      (14, '2024-01-25', '16:15:00', '/path/to/result_allergy_immunology.pdf', 76543, 14, 14),
      (15, '2024-01-29', '11:00:00', '/path/to/result_geriatrics.pdf', 34567, 15, 15),
      (16, '2024-01-30', '13:45:00', '/path/to/result_vascular_surgery.pdf', 65432, 16, 16),
      (17, '2024-01-31', '15:15:00', '/path/to/result_radiation_oncology.pdf', 12345, 17, 17),
      (18, '2024-02-01', '09:30:00', '/path/to/result_developmental_peds.pdf', 54321, 18, 18),
      (19, '2024-02-02', '11:45:00', '/path/to/result_immunology.pdf', 76543, 19, 19),
      (20, '2024-02-01', '16:15:00', '/path/to/result_forensic_pathology.pdf', 23456, 20, 20);
INSERT INTO time_sheet (id, date_of_consultation, start_time_of_consultation, end_time_of_consultation, available, schedule_id)
VALUES(1, '2024-01-15', '10:00:00', '10:15:00', true, 1),
      (2, '2024-01-16', '11:30:00', '11:45:00', true, 2),
      (3, '2024-01-17', '14:15:00', '14:30:00', true, 3),
      (4, '2024-01-18', '09:45:00', '10:00:00', true, 4),
      (5, '2024-01-19', '16:30:00', '16:45:00', false, 5),
      (6, '2024-01-19', '13:00:00', '13:45:00', true, 6),
      (7, '2024-01-19', '10:45:00', '11:30:00', true, 7),
      (8, '2024-01-22', '10:30:00', '11:15:00', true, 8),
      (9, '2024-01-23', '15:15:00', '14:00:00', true, 9),
      (10, '2024-01-24', '08:30:00', '09:15:00', false, 10),
      (11, '2024-01-25', '11:00:00', '12:00:00', true, 11),
      (12, '2024-01-26', '14:45:00', '15:45:00', true, 12),
      (13, '2024-01-25', '09:15:00', '10:15:00', true, 13),
      (14, '2024-01-25', '16:00:00', '17:00:00', true, 14),
      (15, '2024-01-29', '10:30:00', '11:30:00', false, 15),
      (16, '2024-01-30', '13:15:00', '13:45:00', true, 16),
      (17, '2024-01-31', '14:30:00', '16:00:00', true, 17),
      (18, '2024-02-01', '08:45:00', '09:45:00', true, 18),
      (19, '2024-02-02', '11:15:00', '11:15:00', true, 19),
      (20, '2024-02-01', '15:45:00', '15:45:00', false, 20);
INSERT INTO schedule_day_of_week (day_of_week
, is_working_day, schedule_id)
VALUES
    ('MONDAY', true, 1),
    ('TUESDAY', true, 2),
    ('WEDNESDAY', true, 3),
    ('THURSDAY', true, 4),
    ('FRIDAY', true, 5),
    ('SATURDAY', false, 6),
    ('SUNDAY', false, 7),
    ('MONDAY', true, 8),
    ('TUESDAY', true, 9),
    ('WEDNESDAY', true, 10),
    ('THURSDAY', true, 11),
    ('FRIDAY', true, 12),
    ('SATURDAY', false, 13),
    ('SUNDAY', false, 14),
    ('MONDAY', true, 15),
    ('TUESDAY', true, 16),
    ('WEDNESDAY', true, 17),
    ('THURSDAY', true, 18),
    ('FRIDAY', true, 19),
    ('SATURDAY', false, 20),
    ('SUNDAY', false, 21),
    ('MONDAY', true, 22),
    ('TUESDAY', true, 23),
    ('WEDNESDAY', true, 24),
    ('THURSDAY', true, 25);