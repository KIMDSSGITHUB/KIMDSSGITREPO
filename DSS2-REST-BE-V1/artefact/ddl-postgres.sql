-- DDL STATEMENTS

--SCHEMA
drop view DDS_DB_V1;
CREATE schema DSS_DB_V1;

--TABLES
create table REGISTRATIONS
(
    email      varchar(25) not null primary key,
    admin_name varchar(25) not null,
    password   varchar(50) not null,
    phone      varchar(15) not null
);

-- DML

-- QUERIES
SELECT * FROM registrations;