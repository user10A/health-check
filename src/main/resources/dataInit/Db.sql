INSERT INTO user_account (id, email, password, role)
VALUES
    (0,'admin@gmail.com','$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K','ADMIN'),
    (1, 'pavel.morozov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (2, 'aigerim.kozhomberdieva@gmail.com',  '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (3, 'healthcheck2024java11@gmail.com','$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (4, 'cholpon.orozova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (5, 'meerim.davletova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (6, 'irina.smirnova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (7, 'vladimir.orlov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (8, 'olga.petrova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (9, 'sergey.mikhailov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (10, 'kamila.sadyrbekova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (11, 'rysbek.jumabaev@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (12, ' gulzat.bektursunova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (13, 'jyldyz.zhumagulova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (14, ' samat.tilekov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (15, ' nazira.zhunusova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (16, 'murat.abdykerimov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (17, ' dina.kadyrova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (18, 'talant.bektemirov@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (19,'tatiana.novikova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER'),
    (20, ' maria.kuznetsova@gmail.com', '$2a$04$q/5hoHH1.96f6IUIF4oEIO4EAjuLOPvJSa/hPCo5XEERI3cC2pm4K', 'USER');
--  Password=Abcd123!@
INSERT INTO users (id, first_name, last_name, phone_number, user_account_id)
VALUES
    (1, 'Павел', 'Морозов', '+996123456789', 1),
    (2, 'Айгерим', 'Кожомбердиева', '+996987654321', 2),
    (3, 'Бакыт', 'Акматов', '+996111111112', 3),
    (4, 'Чолпон', 'Орозова', '+996222222223', 4),
    (5, 'Меерим', 'Кубатова', '+996333333334', 5),
    (6, 'Ирина', 'Смирнова', '+996444444445', 6),
    (7, 'Владимир', 'Орлов', '+996555555556', 7),
    (8, 'Ольга', 'Петрова', '+996666666667', 8),
    (9, 'Сергей', 'Михайлов', '+996777777778', 9),
    (10, 'Камила', 'Садырбекова', '+996888888889', 10),
    (11, 'Рысбек', 'Жумабаев', '+996999999990', 11),
    (12, 'Гульзат', 'Бектурсунова', '+996100101011', 12),
    (13, 'Жылдыз', 'Жумагулова', '+996122121212', 13),
    (14, 'Самат', 'Тилеков', '+996113131313', 14),
    (15, 'Назира', 'Жунусова', '+996144141414', 15),
    (16, 'Мурат', 'Абдыкеримов', '+996515151515', 16),
    (17, 'Дина', 'Кадырова', '+996116161616', 17),
    (18, 'Талант', 'Бектемиров', '+996117171717', 18),
    (19, 'Татьяна', 'Новикова', '+996181181818', 19),
    (20, 'Мария', 'Кузнецова', '+996191991919', 20);
INSERT INTO application (id, username, date_of_application_creation, phone_number, processed)
VALUES
    (1, 'София Уильямс', '2024-02-04', '+996202020202', false),
    (2, 'Данияр Браун', '2024-02-05', '+996212121212', true),
    (3, 'Ава Джонсон', '2024-02-06', '+996222222222', false),
    (4, 'Джек Дэвис', '2024-02-07', '+996232323232', false),
    (5, 'Гулзат Робертс', '2024-02-08', '+996242422424', true),
    (6, 'Эрнест Тейлор', '2024-02-09', '+996252522525', false),
    (7, 'София Мур', '2024-02-10', '+996262662626', true),
    (8, 'Максат Андерсон', '2024-02-11', '+996227272727', false),
    (9, 'Чыныбек Томас', '2024-02-12', '+996288282828', false),
    (10, 'Уильям Хилл', '2024-02-13', '+996929292929', false),
    (11, 'Эмма Дэвис', '2024-02-14', '+996330303030', false),
    (12, 'Айдар Райт', '2024-02-15', '+996331313131', true),
    (13, 'Элла Джонс', '2024-02-16', '+996322323232', false),
    (14, 'Логан Картер', '2024-02-17', '+996333333333', true),
    (15, 'Эйвери Харрисон', '2024-02-18', '+996344343434', false),
    (16, 'Хенри Эванс', '2024-02-19', '+996353553535', false),
    (17, 'Зоя Паркер', '2024-02-20', '+996363633636', false),
    (18, 'Оуэн Купер', '2024-02-21', '+996373373737', true),
    (19, 'Мила Брайант', '2024-02-22', '+996388383838', false),
    (20, 'Жакшылык Фишер', '2024-02-23','+996939393939', false);

INSERT INTO department (id, facility)
VALUES(1, 'Анестезиология'),
      (2, 'Онкология'),
      (3, 'Терапия'),
      (4, 'Ортопедия'),
      (5, 'Нейрохирургия'),
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
      (20, 'Урология'),
      (21,'Аллергология'),
      (22,'Неврология');
INSERT INTO doctor (id, first_name, last_name, image, position, description, is_active,  department_id)
VALUES
      (1, 'Даниил', 'Федоров', 'https://java112024.s3.eu-north-1.amazonaws.com/1710738990847Group 337693.png', 'Анестезиолог', '<p>Специалист на обеспечении безболезненного и комфортного состояния пациента во время медицинских процедур и операций.<p>', true, 1),
      (2, 'Артемий', 'Жукова', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739143436Group 337691.png', 'Анестезиолог', '<p>Cпециалист на обеспечении безболезненного и комфортного состояния пациента во время медицинских процедур и операций.<p>', true, 1),
      (3, 'Михаил', 'Новиков', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739188040Group 337678.png', 'Анестезиолог', '<p>Cпециалист на обеспечении безболезненного и комфортного состояния пациента во время медицинских процедур и операций.<p>', true, 1),
      (4, 'Анна', 'Морозова', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739503759Group 337672.png', 'Онколог', '<p>Специалист на диагностике, лечении и уходе за пациентами с раковыми заболеваниями.<p>', true, 2),
      (5, 'Дмитрий', 'Кузнецов', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739279753Group 337670.png', 'Онколог', '<p>Специалист на диагностике, лечении и уходе за пациентами с раковыми заболеваниями.<p>', true, 2),
      (6, 'Конор', 'Магрегор', 'https://java112024.s3.eu-north-1.amazonaws.com/1710738988241image 15.png', 'Онколог', '<p>Специалист на диагностике, лечении и уходе за пациентами с раковыми заболеваниями.<p>', true, 2),
      (7, 'Эмма', 'Джонсон', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739663725Group 337690.png', 'Терапевт', '<p>Специалист на предоставлении первичной медицинской помощи.<p>', true, 3),
      (8, 'Максим', 'Галкин', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739591869Group 337664.png', 'Терапевт', '<p>Cпециалист на предоставлении первичной медицинской помощи.<p>', true, 3),
      (9, 'Ирина', 'Кайратова', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739284075Group 337661.png', 'Терапевт', '<p>Специалист на предоставлении первичной медицинской помощи.<p>', true, 3),
      (10, 'Александр', 'Мартинес', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739279753Group 337670.png', 'Ортопед', '<p>Специализируется на лечении травм и болезней опорно-двигательного аппарата, включая костные переломы и суставные проблемы.<p>', true, 4),
      (11, 'София', 'Белова', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Ортопед', '<p>Специализируется на лечении травм и болезней опорно-двигательного аппарата, включая костные переломы и суставные проблемы.<p>', true, 4),
      (12, 'Максим', 'Лебедев', 'https://java112024.s3.eu-north-1.amazonaws.com/1710740343181Group 337676.png', 'Ортопед', '<p>Специализируется на лечении травм и болезней опорно-двигательного аппарата, включая костные переломы и суставные проблемы.<p>', true, 4),
      (13, 'Грейс', 'Нельсон', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739889179Group 337666.png', 'Нейрохирург', '<p>Занимается хирургическим лечением заболеваний и повреждений нервной системы, включая мозг и спинной мозг.<p>', true, 5),
      (14, 'Андрей', 'Картер', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Аллерголог', '<p>Отвечает за вакцинацию и иммунизацию пациентов для предотвращения инфекционных заболеваний.<p>', true, 6),
      (15, 'Мия', 'Купер', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739840514Group 337665.png', 'Оториноларинголог', '<p>Специализируется на диагностике и лечении заболеваний уха, горла и носа.<p>', true, 7),
      (16, 'Лиам', 'Робинсон', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Флеболог', '<p>Занимается диагностикой и лечением заболеваний вен и кровеносной системы.<p>', true, 8),
      (17, 'Ария', 'Хилл', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739969097Group 337667.png', 'Флеболог', '<p>Занимается диагностикой и лечением заболеваний вен и кровеносной системы.<p>', true, 8),
      (18, 'Генри', 'Райт', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Гинеколог', '<p>Осуществляет диагностику и лечение заболеваний женских половых органов и системы размножения.<p>', true, 9),
      (19, 'Элла', 'Фишер', 'https://java112024.s3.eu-north-1.amazonaws.com/1710740104838Group 337669.png', 'Офтальмолог', '<p>Специализируется на диагностике и лечении заболеваний глаз и зрительной системы.<p>', true, 10),
      (20, 'Бенджамин', 'Лопес', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png','Эндокринолог', '<p>Занимается диагностикой и лечением заболеваний эндокринной системы и желез внутренней секреции.<p>', true, 11),
      (21, 'Лили', 'Бейкер', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Дерматолог', '<p>Отвечает за диагностику и лечение заболеваний кожи, волос и ногтей.<p>', true, 12),
      (22, 'Уильям', 'Гомес', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Проктолог', '<p>Специализируется на диагностике и лечении заболеваний прямой кишки и прямой кишечной области.<p>', true, 13),
      (23, 'Зоя', 'Перес', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Физиотерапевт', '<p>Предоставляет физическую терапию и реабилитацию пациентов после травм и операций.<p>', true, 14),
      (24, 'Сэмюэл', 'Рассел', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png','Кардиолог', '<p>Занимается диагностикой и лечением заболеваний сердца и сосудов.<p>', true, 15),
      (25, 'Виктория', 'Фостер', 'https://java112024.s3.eu-north-1.amazonaws.com/1710739799947Group 337677.png', 'Психотерапевт', '<p>Оказывает психотерапевтическую помощь пациентам с психическими расстройствами и проблемами.<p>', true, 16);
INSERT INTO schedule (id, start_date_work, end_date_work, start_day_time, end_day_time, start_break_time, end_break_time, interval_in_minutes, department_id, doctor_id)
VALUES
    (1, '2024-03-15', '2024-04-15', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 1, 1),
    (2, '2024-03-16', '2024-04-16', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 1, 2),
    (3, '2024-03-17', '2024-04-17', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 1, 3),
    (4, '2024-03-18', '2024-04-18', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 2, 4),
    (5, '2024-03-19', '2024-04-19', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 2, 5),
    (6, '2024-03-19', '2024-04-20', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 2, 6),
    (7, '2024-03-19', '2024-04-21', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 3, 7),
    (8, '2024-03-22', '2024-04-22', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 3, 8),
    (9, '2024-03-23', '2024-04-23', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 3, 9),
    (10, '2024-03-24', '2024-04-24', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FOURTYFIVE', 4, 10),
    (11, '2024-03-25', '2024-04-25', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 4, 11),
    (12, '2024-03-26', '2024-04-26', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 4, 12),
    (13, '2024-03-25', '2024-04-27', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 5, 13),
    (14, '2024-03-25', '2024-04-28', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 6, 14),
    (15, '2024-03-29', '2024-04-29', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'SIXTY', 7, 15),
    (16, '2024-03-30', '2024-05-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 8, 16),
    (17, '2024-03-31', '2024-05-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 8, 17),
    (18, '2024-04-01', '2024-05-01', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 9, 18),
    (19, '2024-04-02', '2024-05-02', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 10, 19),
    (20, '2024-04-01', '2024-05-03', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THIRTY', 11, 20),
    (21, '2024-04-01', '2024-05-04', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 12, 21),
    (22, '2024-04-05', '2024-05-05', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 13, 22),
    (23, '2024-04-06', '2024-05-06', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 14, 23),
    (24, '2024-04-07', '2024-05-07', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 15, 24),
    (25, '2024-04-08', '2024-05-08', '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'NINETY', 16, 25);

INSERT INTO appointment (id, appointment_date, appointment_time, status, processed, user_id, department_id, doctor_id)
VALUES
    (1, '2024-03-15', '10:00:00', 'CANCELLED', false, 1, 1, 1),
    (2, '2024-03-16', '11:30:00', 'CONFIRMED', false, 2, 1, 2),
    (3, '2024-03-17', '14:15:00', 'CONFIRMED', false, 3, 1, 3),
    (4, '2024-03-18', '09:45:00', 'FINISHED', true, 4, 2, 4),
    (5, '2024-03-19', '16:30:00', 'CANCELLED', false, 5, 2, 5),
    (6, '2024-03-19', '13:00:00', 'CONFIRMED', false, 6, 2, 6),
    (7, '2024-03-19', '10:45:00', 'CANCELLED', false, 7, 3, 7),
    (8, '2024-03-19', '10:30:00', 'CONFIRMED', false, 8, 3, 8),
    (9, '2024-03-23', '15:15:00', 'FINISHED', true, 9, 3, 9),
    (10, '2024-03-24', '08:30:00', 'CONFIRMED', false, 10, 4, 10),
    (11, '2024-03-25', '11:00:00', 'CANCELLED', false, 11, 4, 11),
    (12, '2024-03-26', '14:45:00', 'CONFIRMED', false, 12, 4, 12),
    (13, '2024-03-27', '09:15:00', 'FINISHED', true, 13, 5, 13),
    (14, '2024-03-28', '16:00:00', 'CONFIRMED', false, 14, 6, 14),
    (15, '2024-03-29', '10:30:00', 'CANCELLED', false, 15, 7, 15),
    (16, '2024-03-30', '13:15:00', 'CONFIRMED', false, 16, 8, 16),
    (17, '2024-03-31', '14:30:00', 'FINISHED', true, 17, 8, 17),
    (18, '2024-04-01', '08:45:00', 'CONFIRMED', false, 18, 9, 18),
    (19, '2024-04-02', '11:15:00', 'CANCELLED', false, 19, 10, 19),
    (20, '2024-04-01', '15:45:00', 'CONFIRMED', false, 20, 11, 20);

INSERT INTO result (id, result_date, time_of_uploading_result, pdf_url, result_number, department_id, user_id)
VALUES
    (1, '2024-03-15', '10:30:00', '/path/to/result.pdf', 1234500001, 1, 1),
    (2, '2024-03-16', '12:00:00', '/path/to/result_cardiology.pdf', 5432100002, 1, 2),
    (3, '2024-03-17', '14:45:00', 'https://java112024.s3.eu-north-1.amazonaws.com/1711175173295PDF.pdf', 6789000003, 1, 3),
    (4, '2024-03-18', '11:15:00', '/path/to/result_orthopedics.pdf', 9876500004, 2, 4),
    (5, '2024-03-19', '16:45:00', '/path/to/result_pediatrics.pdf', 5678900005, 2, 5),
    (6, '2024-03-19', '13:30:00', '/path/to/result_urology.pdf', 4321000006, 2, 6),
    (7, '2024-03-19', '11:00:00', '/path/to/result_ophthalmology.pdf', 1098700007, 3, 7),
    (8, '2024-03-22', '13:45:00', '/path/to/result_endocrinology.pdf', 6789000008, 3, 8),
    (9, '2024-03-23', '15:30:00', '/path/to/result_rheumatology.pdf', 3456700009, 3, 9),
    (10, '2024-03-24', '08:45:00', '/path/to/result_ent.pdf', 8765400010, 4, 10),
    (11, '2024-03-25', '11:15:00', '/path/to/result_psychiatry.pdf', 2345600011, 4, 11),
    (12, '2024-03-26', '15:00:00', '/path/to/result_gynecology.pdf', 8765400012, 4, 12),
    (13, '2024-03-27', '09:30:00', '/path/to/result_pulmonology.pdf', 1234500013, 5, 13),
    (14, '2024-03-28', '16:15:00', '/path/to/result_allergy_immunology.pdf', 7654300014, 6, 14),
    (15, '2024-03-29', '11:00:00', '/path/to/result_geriatrics.pdf', 3456700015, 7, 15),
    (16, '2024-03-30', '13:45:00', '/path/to/result_vascular_surgery.pdf', 6543200016, 8, 16),
    (17, '2024-03-31', '15:15:00', '/path/to/result_radiation_oncology.pdf', 1234500017, 8, 17),
    (18, '2024-04-01', '09:30:00', '/path/to/result_developmental_peds.pdf', 5432100018, 9, 18),
    (19, '2024-04-02', '11:45:00', '/path/to/result_immunology.pdf', 7654300019, 10, 19),
    (20, '2024-04-01', '16:15:00', '/path/to/result_immunologyasdfasd.pdf', 2345600020, 11, 20);

INSERT INTO time_sheet (id, date_of_consultation, start_time_of_consultation, end_time_of_consultation, available, schedule_id)
VALUES
      (1, '2024-03-15', '10:00:00', '10:15:00', true, 1),
      (2, '2024-03-16', '11:30:00', '11:45:00', true, 2),
      (3, '2024-03-17', '14:15:00', '14:30:00', true, 3),
      (4, '2024-03-18', '09:45:00', '10:00:00', true, 4),
      (5, '2024-03-19', '16:30:00', '16:45:00', false, 5),
      (6, '2024-03-19', '13:00:00', '13:45:00', true, 6),
      (7, '2024-03-19', '10:45:00', '11:30:00', true, 7),
      (8, '2024-03-22', '10:30:00', '11:15:00', true, 8),
      (9, '2024-03-23', '15:15:00', '14:00:00', true, 9),
      (10, '2024-03-24', '08:30:00', '09:15:00', false, 10),
      (11, '2024-03-25', '11:00:00', '12:00:00', true, 11),
      (12, '2024-03-26', '14:45:00', '15:45:00', true, 12),
      (13, '2024-03-25', '09:15:00', '10:15:00', true, 13),
      (14, '2024-03-25', '16:00:00', '17:00:00', true, 14),
      (15, '2024-03-29', '10:30:00', '11:30:00', false, 15),
      (16, '2024-03-30', '13:15:00', '13:45:00', true, 16),
      (17, '2024-03-31', '14:30:00', '16:00:00', true, 17),
      (18, '2024-04-01', '08:45:00', '09:45:00', true, 18),
      (19, '2024-04-02', '11:15:00', '11:15:00', true, 19),
      (20, '2024-04-01', '15:45:00', '15:45:00', false, 20);

INSERT INTO schedule_day_of_week (day_of_week, is_working_day, schedule_id)
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
INSERT INTO feedback (id, doctor_id, user_id, local_date, rating, comment)
VALUES
    (1, 1, 1, '2024-03-25', 5, 'Отличный врач!'),
    (2, 2, 1, '2024-03-24', 4, 'Хороший врач, но пришлось ждать'),
    (3, 3, 3, '2024-03-23', 3, 'Средний врач'),
    (4, 4, 4, '2024-03-22', 2, 'Не очень хороший врач'),
    (5, 5, 5, '2024-03-21', 1, 'Ужасный врач!'),
    (6, 6, 6, '2024-03-20', 5, 'Великолепный врач!'),
    (7, 7, 7, '2024-03-19', 4, 'Неплохой врач, но могло быть лучше'),
    (8, 8, 8, '2024-03-18', 3, 'Средний врач'),
    (9, 9, 9, '2024-03-17', 2, 'Не рекомендую этого врача'),
    (10, 10, 10, '2024-03-16', 1, 'Самый ужасный врач!'),
    (11, 1, 9, '2024-03-15', 5, 'Отличный врач!'),
    (12, 2, 8, '2024-03-14', 4, 'Хороший врач, но пришлось ждать'),
    (13, 3, 7, '2024-03-13', 3, 'Средний врач'),
    (14, 4, 6, '2024-03-12', 2, 'Не очень хороший врач'),
    (15, 5, 5, '2024-03-11', 1, 'Ужасный врач!'),
    (16, 6, 4, '2024-03-10', 5, 'Великолепный врач!'),
    (17, 7, 2, '2024-03-09', 4, 'Неплохой врач, но могло быть лучше'),
    (18, 8, 3, '2024-03-08', 3, 'Средний врач'),
    (19, 9, 2, '2024-03-07', 2, 'Не рекомендую этого врача'),
    (20, 1, 1, '2024-03-06', 1, 'Самый ужасный врач!');


