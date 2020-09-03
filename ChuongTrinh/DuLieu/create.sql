create database certificate_university_history;

use certificate_university_history;

-- Create table contains certificate data
create table history_certificate(
	id int auto_increment primary key,
    name varchar(256),
    dateOfBirth varchar(256),
    hometown varchar(256),
    class varchar(256),
    modeOfStudy varchar(256),
    registerNumber varchar(256),    
    idCertificate varchar(256),
    note varchar(256)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into history_certificate (id, name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, idCertificate, note) values
(0, 'test name', '01/01/1990', 'Nghệ An', 'Kinh tế K24', 'Chính quy', '134', '708342', '');

-- Create table contains account data to login admin page
create table login(
	id int auto_increment primary key,
    username varchar(256) not null,
    password varchar(256) not null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

insert into login(username, password) values("admin","admin");