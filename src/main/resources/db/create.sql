SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS sections (
  id int PRIMARY KEY auto_increment,
  description VARCHAR,
  departmentId int,

  completed BOOLEAN
);

CREATE TABLE IF NOT EXISTS departments (
  id int PRIMARY KEY auto_increment,
  name VARCHAR
);
