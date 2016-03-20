CREATE TABLE trupp(
id serial primary key,
name varchar(40) not null,
email varchar(60) unique
);
