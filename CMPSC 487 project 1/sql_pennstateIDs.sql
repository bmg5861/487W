drop database if exists PennState_IDs;
create database PennState_IDs;
use PennState_IDs;

set names utf8;
set character_set_client = utf8mb4;

create table users (
ID varchar(11) not null,
user_name varchar(50) not null,
user_type varchar(50) not null,
user_status boolean not null,
primary key (ID)
);

insert into users
values ('%A933462891',"John Doe","Student", true),
       ("%A918710159","Leonhard Euler", "Teacher", true),
	   ("%A960200528","Sukmoon Chang","Teacher", true),
       ("%A941360688","Albert Einstein","Teacher", true),
       ("%A937240676","Alan Turing","Teacher", false),
       ("%A935059743","James Bool","Teacher", false),
       ("%A944019072","David Hilbert","Student", true),
       ("%A963072628","Andrew Wiles","Student", true),
       ("%A984649658","Pablo Picasso","Student", false),
       ("%A942434991","Andy Warhol","Student", false),
       ("%A990927247","James Riley","Admin", true);
       
create table access (
ID varchar(11) not null,
user_name varchar(50) not null,
user_type varchar(50) not null,
swipe_time timestamp not null,
swipe_type varchar(3) not null
);

insert into access
values ('%A933462891',"John Doe","Student", timestamp("23-09-15", "10:54:13"), "In"),
       ('%A933462891',"John Doe","Student", timestamp("23-09-15", "11:54:13"), "Out"),
       ("%A918710159","Leonhard Euler", "Teacher", timestamp("23-09-13", "12:54:13"), "In"),
       ("%A918710159","Leonhard Euler", "Teacher", timestamp("23-09-13", "13:54:13"), "Out"),
       ("%A960200528","Sukmoon Chang","Teacher", timestamp("23-09-14", "15:54:13"), "In"),
       ("%A960200528","Sukmoon Chang","Teacher", timestamp("23-09-14", "17:54:13"), "Out");

