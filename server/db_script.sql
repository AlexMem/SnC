create table users_1
(
	id serial not null
		constraint users_1_pk
			primary key,
	name varchar(200) not null
);

alter table users_1 owner to postgres;

create unique index users_1_id_uindex
	on users_1 (id);

create table maze_1
(
	id serial not null
		constraint maze_1_pk
			primary key,
	user_id integer not null,
	result double precision,
	date_time timestamp
);

comment on table maze_1 is 'Лабиринт';

alter table maze_1 owner to postgres;

create unique index maze_1_id_uindex
	on maze_1 (id);

create table stencil_1
(
	id serial not null
		constraint stencil_1_pk
			primary key,
	user_id integer not null,
	result double precision not null,
	date_time timestamp
);

comment on table stencil_1 is 'Трафарет';

alter table stencil_1 owner to postgres;

create unique index stencil_1_id_uindex
	on stencil_1 (id);

create table rolling_ball_1
(
	id serial not null
		constraint rolling_ball_1_pk
			primary key,
	user_id integer not null,
	result double precision,
	date_time timestamp
);

comment on table rolling_ball_1 is 'Платформа';

alter table rolling_ball_1 owner to postgres;

create unique index rolling_ball_1_id_uindex
	on rolling_ball_1 (id);

create table unlock_statistic_1
(
	id integer,
	row_num integer,
	user_id integer not null,
	point_x double precision,
	point_y double precision,
	date_time timestamp,
	uniq_id serial not null
		constraint unlock_statistic_1_pk
			primary key
);

alter table unlock_statistic_1 owner to postgres;

create unique index unlock_statistic_1_uniq_id_uindex
	on unlock_statistic_1 (uniq_id);

