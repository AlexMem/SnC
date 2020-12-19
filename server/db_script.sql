create table users
(
	id serial not null
		constraint users_pk
			primary key,
	name varchar(200) not null
);

alter table users owner to postgres;

create unique index users_id_uindex
	on users (id);

create table maze
(
	id serial not null
		constraint maze_pk
			primary key,
	user_id integer not null,
	result double precision,
	date_time timestamp
);

comment on table maze is 'Лабиринт';

alter table maze owner to postgres;

create unique index maze_id_uindex
	on maze (id);

create table stencil
(
	id serial not null
		constraint stencil_pk
			primary key,
	user_id integer not null,
	result double precision not null,
	date_time timestamp
);

comment on table stencil is 'Трафарет';

alter table stencil owner to postgres;

create unique index stencil_id_uindex
	on stencil (id);

create table rolling_ball
(
	id serial not null
		constraint rolling_ball_pk
			primary key,
	user_id integer not null,
	result double precision,
	date_time timestamp
);

comment on table rolling_ball is 'Катать шар';

alter table rolling_ball owner to postgres;

create unique index rolling_ball_id_uindex
	on rolling_ball (id);

create table unlock_statistic
(
	id serial,
	row_num integer,
	user_id integer not null,
	point_x double precision,
	point_y double precision,
	date_time timestamp,
	uniq_id serial not null
		constraint unlock_statistic_pk
			primary key
);

alter table unlock_statistic owner to postgres;

create unique index unlock_statistic_uniq_id_uindex
	on unlock_statistic (uniq_id);

