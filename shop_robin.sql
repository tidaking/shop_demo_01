START TRANSACTION;

CREATE DATABASE shop_demo_01;
USE shop_demo_01;


CREATE TABLE USER
(
uid INT(10) PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(20) NOT NULL UNIQUE,
PASSWORD VARCHAR(20) NOT NULL,
NAME VARCHAR(20) NOT NULL,
email VARCHAR(50) NOT NULL,
telephone INT(11) NOT NULL,
birthday DATE,
sex VARCHAR(10),
state BOOLEAN NOT NULL,
CODE VARCHAR(10)
);


CREATE TABLE orders
(
oid INT(10) PRIMARY KEY AUTO_INCREMENT,
ordertime DATE NOT NULL,
total DOUBLE NOT NULL,
NAME VARCHAR(20) NOT NULL,
address VARCHAR(50) NOT NULL,
telephone INT(11) NOT NULL,
uid INT(10) NOT NULL
);


CREATE TABLE orderitem
(
itemid INT(10) PRIMARY KEY AUTO_INCREMENT,
COUNT INT(10) NOT NULL,
subtotal DOUBLE NOT NULL,
pid INT(10) NOT NULL,
oid INT(10) NOT NULL
);

CREATE TABLE product
(
pid INT(10) PRIMARY KEY AUTO_INCREMENT,
pname VARCHAR(20) NOT NULL,
market_price DOUBLE NOT NULL,
shop_price DOUBLE NOT NULL,
pimage VARCHAR(100) NOT NULL,
pdate DATE NOT NULL,
is_hot BOOLEAN NOT NULL,
pdesc VARCHAR(200),
pflag BOOLEAN NOT NULL,
cid INT(10) NOT NULL

);

CREATE TABLE category
(
cid INT(10) PRIMARY KEY AUTO_INCREMENT,
cname VARCHAR(20) NOT NULL
);

ALTER TABLE orders ADD CONSTRAINT orders2user_fk_uid FOREIGN KEY(uid)REFERENCES USER(uid);
ALTER TABLE orderitem ADD CONSTRAINT orderitem2orders_fk_oid FOREIGN KEY(oid)REFERENCES orders(oid);
ALTER TABLE orderitem ADD CONSTRAINT orderitem2orders_fk_pid FOREIGN KEY(pid)REFERENCES product(pid);
ALTER TABLE product ADD CONSTRAINT product2category_fk_cid FOREIGN KEY(cid)REFERENCES category(cid);

COMMIT;