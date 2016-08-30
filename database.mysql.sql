SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone="+00:00";

CREATE TABLE cnt_categorize
(
  food_id INT(11) NOT NULL,
  category_id INT(11) NOT NULL
);

CREATE TABLE cnt_category
(
  id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  title VARCHAR(255),
  description VARCHAR(255)
);

CREATE TABLE cnt_food
(
  id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  title VARCHAR(255),
  description VARCHAR(255),
  is_active BOOL NOT NULL DEFAULT TRUE,
  price NUMERIC,
  image VARCHAR(255)
);

CREATE TABLE cnt_order
(
  id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_pin VARCHAR(255) NOT NULL,
  schedule_id INT(11) NOT NULL,
  `status` VARCHAR(4) NOT NULL DEFAULT 'o' -- o = ordered...
);

CREATE TABLE cnt_ordered_food
(
  order_id INT(11) NOT NULL,
  food_id INT(11) NOT NULL,
  price DOUBLE PRECISION NOT NULL DEFAULT 0.0 -- the current price of the food at the time of the order
);

CREATE TABLE cnt_permission
(
  pin INT(11) NOT NULL,
  `type` VARCHAR(1) NOT NULL
);

CREATE TABLE cnt_schedule
(
  id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  DATE DATE
);

CREATE TABLE cnt_scheduled_food
(
  schedule_id INT(11) NOT NULL,
  food_id INT(11) NOT NULL,
  servings_left INT(11) NOT NULL DEFAULT 0,
  total_servings INT(11) NOT NULL DEFAULT 0
);

CREATE TABLE cnt_payment
(
  id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  user_pin VARCHAR(255) NOT NULL,
  DATE TIMESTAMP NOT NULL DEFAULT NOW(),
  amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
  order_id INT(11),
  comments VARCHAR(255)
);

