SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS tasks (
  id int PRIMARY KEY auto_increment,
  description VARCHAR,
  categoryId int,
  completed BOOLEAN
);

CREATE TABLE IF NOT EXISTS categories (
  id int PRIMARY KEY auto_increment,
  name VARCHAR
);


CREATE TABLE IF NOT EXISTS employees (
  id int PRIMARY KEY auto_increment,
  name VARCHAR,
  ek_no int,
  position VARCHAR,
);