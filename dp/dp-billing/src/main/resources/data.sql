insert into contracts(id, contract_number, name, address)
values (1, '123','Рога и Копыта', 'Москва Тверская 1'), (2, '124','Иванов Иван Иванович', 'Москва Б. Академическая 1')
     , (3, '125','Виктор Викторович Викторов', 'Москва ВДНХ');
ALTER TABLE contracts ALTER COLUMN id RESTART WITH 4;

insert into services(id, name)
values (1, 'Local  call'), (2, 'Long-distance call'), (3, 'International call');

insert into tariff_plans(id, name)
values (1, 'Life'), (2, 'Economy');

insert into costs(service_id, tpt, cost, tariff_plan_id)
values (1, '7495', 0, 1), (2, '7812', 0.5, 1), (2, '7', 2, 1), (3, '3', 5, 1),
(1, '7495', 0, 2), (2, '7812', 0.2, 2), (2, '7', 1, 2), (3, '3', 3, 2);

insert into telephone_numbers(id, number, contract_id, tariff_plan_id)
values (1, '74999950304', 1, 1), (2, '74999950305', 1, 2), (3, '74999950301', 2, 2), (4, '74999950302', 3, 2);
ALTER TABLE telephone_numbers ALTER COLUMN id RESTART WITH 5;

insert into traffic_daily(cdate, service_id, telephone_number_id, bnumber, duration, amount)
values (PARSEDATETIME('20260417115303', 'yyyyMMddHHmmss'),  1, 1, '74951920041', 1, 1);

insert into users(id, username, password)
values (1, 'admin', '1'), (2, '123', '123'), (3, 'user2', '1'), (4, 'user3', '1'), (5, 'k', 'k');
insert into roles(id, name)
values(1, 'ADMIN'), (2, 'CLIENT');

insert into user_role(user_id, role_id)
values(1,1), (1,2), (2,2), (3,2), (4,2), (5,1), (5,2);