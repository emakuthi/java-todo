SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS sections (
  id int PRIMARY KEY auto_increment,
  description VARCHAR,
  categoryId int,

  completed BOOLEAN
);

CREATE TABLE IF NOT EXISTS categories (
  id int PRIMARY KEY auto_increment,
  name VARCHAR
);
