create table users
(
    id   serial      not null
        constraint users_pk
            primary key,
    name varchar(30) not null
);

alter table users
    owner to bnexuuveperybj;

create unique index users_id_uindex
    on users (id);

create table maze
(
    id        serial  not null
        constraint maze_pk
            primary key,
    user_id   integer not null,
    result    double precision,
    date_time timestamp
);

comment on table maze is 'Лабиринт';

alter table maze
    owner to bnexuuveperybj;

create unique index maze_id_uindex
    on maze (id);

create table stencil
(
    id        serial           not null
        constraint stencil_pk
            primary key,
    user_id   integer          not null,
    result    double precision not null,
    date_time timestamp
);

comment on table stencil is 'Трафарет';

alter table stencil
    owner to bnexuuveperybj;

create unique index stencil_id_uindex
    on stencil (id);

create table rolling_ball
(
    id        serial  not null
        constraint rolling_ball_pk
            primary key,
    user_id   integer not null,
    result    double precision,
    date_time timestamp
);


alter table rolling_ball
    owner to bnexuuveperybj;

create unique index rolling_ball_id_uindex
    on rolling_ball (id);

create table unlock_statistic
(
    id       serial  not null,
    user_id  integer not null,
    points   double precision[],
    date_ime timestamp
);

alter table unlock_statistic
    owner to bnexuuveperybj;

create unique index unlock_statistic_id_uindex
    on unlock_statistic (id);

