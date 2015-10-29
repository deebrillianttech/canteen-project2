-- Table: cnt_categorize
CREATE TABLE cnt_categorize
(
  food_id integer NOT NULL,
  category_id integer NOT NULL,
  CONSTRAINT pk_categorize PRIMARY KEY (food_id, category_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_categorize
  OWNER TO postgres;


-- Table: cnt_category
CREATE TABLE cnt_category
(
  id serial NOT NULL,
  title character varying,
  description character varying,
  CONSTRAINT pk_cat PRIMARY KEY (id),
  CONSTRAINT cnt_cat_title_key UNIQUE (title)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_category
  OWNER TO postgres;


-- Table: cnt_food
CREATE TABLE cnt_food
(
  id serial NOT NULL,
  title character varying,
  description character varying,
  is_active boolean NOT NULL DEFAULT true,
  price numeric,
  image character varying,
  CONSTRAINT pk_food PRIMARY KEY (id),
  CONSTRAINT cnt_food_title_key UNIQUE (title)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_food
  OWNER TO postgres;



-- Table: cnt_order
CREATE TABLE cnt_order
(
  id serial NOT NULL,
  user_pin character varying NOT NULL,
  schedule_id integer NOT NULL,
  status character(4) NOT NULL DEFAULT 'o'::bpchar, -- o = ordered...
  CONSTRAINT cnt_order_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_order
  OWNER TO postgres;
COMMENT ON COLUMN cnt_order.status IS 'o = ordered
d = delivered';


-- Table: cnt_ordered_food
CREATE TABLE cnt_ordered_food
(
  order_id integer NOT NULL,
  food_id integer NOT NULL,
  price double precision NOT NULL DEFAULT 0.0, -- the current price of the food at the time of the order
  CONSTRAINT cnt_ordered_food_pkey PRIMARY KEY (order_id, food_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_ordered_food
  OWNER TO postgres;
COMMENT ON COLUMN cnt_ordered_food.price IS 'the current price of the food at the time of the order';


-- Table: cnt_permission
CREATE TABLE cnt_permission
(
  pin integer NOT NULL,
  type character(1) NOT NULL,
  CONSTRAINT cnt_permission_pkey PRIMARY KEY (pin, type)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_permission
  OWNER TO postgres;


-- Table: cnt_schedule
CREATE TABLE cnt_schedule
(
  id serial NOT NULL,
  date date,
  CONSTRAINT cnt_schedule_pkey PRIMARY KEY (id),
  CONSTRAINT cnt_schedule_date_key UNIQUE (date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_schedule
  OWNER TO postgres;


-- Table: cnt_scheduled_food
CREATE TABLE cnt_scheduled_food
(
  schedule_id integer NOT NULL,
  food_id integer NOT NULL,
  servings_left integer NOT NULL DEFAULT 0,
  total_servings integer NOT NULL DEFAULT 0,
  CONSTRAINT cnt_scheduled_food_pkey PRIMARY KEY (schedule_id, food_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_scheduled_food
  OWNER TO postgres;



 -- Table: cnt_payment
CREATE TABLE cnt_payment
(
  id serial NOT NULL,
  user_pin character varying NOT NULL,
  date timestamp without time zone NOT NULL DEFAULT now(),
  amount double precision NOT NULL DEFAULT 0.0,
  order_id integer,
  comments character varying,
  CONSTRAINT cnt_payment_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cnt_payment
  OWNER TO postgres;
