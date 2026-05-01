create table contracts (
                           id bigserial,
                           contract_number varchar(255),
                           name varchar(255),
                           address varchar(255),
                           primary key (id)
);

create table tariff_plans (id bigserial,
                           name varchar(255),
                           primary key (id)
);

create table services (id bigserial,
                       name varchar(255),
                       primary key (id)
);

create table costs (id bigserial,
                    service_id bigint references services (id) on delete cascade,
                    tpt varchar(255),
                    cost numeric(10, 2),
                    tariff_plan_id bigint references tariff_plans (id),
                    primary key (id)
);

create table telephone_numbers (
                         id bigserial,
                         number varchar(255),
                         contract_id bigint references contracts (id),
                         tariff_plan_id bigint references tariff_plans (id),
                         primary key (id)
);

create table if not exists traffic_daily (
                                        id bigserial,
                                        cdate timestamp,
                                        service_id bigint references services (id),
                                        telephone_number_id bigint references telephone_numbers (id),
                                        bnumber varchar(255),
                                        duration numeric(10, 2),
                                        amount numeric(10, 2),
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
    user_id bigint not null,
    role_id bigint not null
);