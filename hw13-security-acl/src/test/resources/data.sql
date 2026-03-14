insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(id, title, author_id, genre_id)
values (1, 'BookTitle_1', 1, 1), (2, 'BookTitle_2', 2, 2), (3, 'BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Хорошая книга', 1), ('Очень хорошая книга', 1),
       ('Нормальная книга', 2), ('Прекрасная книга', 2),
       ('Великолепная книга', 3);

insert into users(id, username, password)
values (1, 'admin', '1'), (2, 'user1', '1'), (3, 'user2', '1'), (4, 'user3', '1');
insert into roles(id, name)
values(1, 'ADMIN'), (2, 'AUTHOR1'), (3, 'AUTHOR2'), (4, 'AUTHOR3');

insert into user_role(user_id, role_id)
values(1,1), (1,2), (1,3), (1,4), (2,2), (3,3), (4,4);

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'admin'),
(2, 0, 'ROLE_ADMIN'),
(3, 0, 'ROLE_AUTHOR1'),
(4, 0, 'ROLE_AUTHOR2'),
(5, 0, 'ROLE_AUTHOR3'),
(6, 1, 'user1'),
(7, 1, 'user2'),
(8, 1, 'user3');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 2, 0),
(2, 1, 2, NULL, 2, 0),
(3, 1, 3, NULL, 2, 0);

ALTER TABLE acl_object_identity ALTER COLUMN id RESTART WITH 4;

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, 1, 1, 1),
(2, 1, 2, 2, 1, 1, 1, 1),
(3, 1, 3, 2, 2, 1, 1, 1),
(4, 1, 4, 3, 1, 1, 1, 1),

(5, 2, 1, 1, 1, 1, 1, 1),
(6, 2, 2, 2, 1, 1, 1, 1),
(7, 2, 3, 2, 2, 1, 1, 1),
(8, 2, 4, 4, 1, 1, 1, 1),

(9, 3, 1, 1, 1, 1, 1, 1),
(10, 3, 2, 2, 1, 1, 1, 1),
(11, 3, 3, 2, 2, 1, 1, 1),
(12, 3, 4, 5, 1, 1, 1, 1);

ALTER TABLE acl_entry ALTER COLUMN id RESTART WITH 13;
