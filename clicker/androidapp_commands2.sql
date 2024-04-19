create database if not exists clicker;
use clicker;

drop table if exists responses;
drop table if exists currentQn;

create table responses (
	id int auto_increment primary key,
	questionNo int,
	choice VARCHAR(1)
	);

create table currentQn (
	question int
	);

insert into currentQn values (0);

# DUMMY DATA	

insert into responses(questionNo, choice) values 
	(1, 'A'),
	(1, 'B'),
	(1, 'C'),
	(1, 'D'),
	(1, 'A'),
	(1, 'B'),
	(2, 'C'),
	(2, 'D'),
	(2, 'A'),
	(2, 'B'),
	(2, 'C'),
	(2, 'D'),
	(3, 'A'),
	(3, 'B'),
	(3, 'C'),
	(3, 'D'),
	(3, 'A'),
	(4, 'B'),
	(4, 'C'),
	(4, 'D'),
	(4, 'A'),
	(4, 'B'),
	(4, 'C'),
	(4, 'D'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A'),
	(5, 'A');
	

