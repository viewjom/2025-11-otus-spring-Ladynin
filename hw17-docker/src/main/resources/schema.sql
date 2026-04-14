create table authors (
                         id bigserial,
                         full_name varchar(255),
                         primary key (id)
);

create table genres (
                        id bigserial,
                        name varchar(255),
                        primary key (id)
);

create table books (
                       id bigserial,
                       title varchar(255),
                       author_id bigint references authors (id) on delete cascade,
                       genre_id bigint references genres(id) on delete cascade,
                       primary key (id)
);

create table if not exists comments (
                                        id bigserial,
                                        book_id bigint references books (id) on delete cascade,
                                        text varchar(255),
                                        primary key (id)
);

create table roles
(
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table users
(
    id bigserial,
    username varchar(255) not null,
    password varchar(500) not null,
    primary key (id)
);

create table user_role
(
    user_id  bigserial not null,
    role_id bigserial not null
);