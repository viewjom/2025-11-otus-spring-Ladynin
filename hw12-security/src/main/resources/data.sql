insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Хорошая книга', 1), ('Очень хорошая книга', 1),
       ('Нормальная книга', 2), ('Прекрасная книга', 2),
       ('Великолепная книга', 3);

insert into users(id, username, password)
values(1, 'user', '1'), (2, 'admin', '1');
insert into roles(id, name)
values(1, 'USER'), (2, 'ADMIN');

insert into user_role(user_id, role_id)
values(1,1), (2,1), (2,2);
