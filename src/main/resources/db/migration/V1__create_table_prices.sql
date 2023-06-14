--create table BRAND
CREATE TABLE brand (
    id INT PRIMARY KEY,
    name VARCHAR(60)
);
--create table PRICES
CREATE TABLE PRICES (
    brand_id INTEGER,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    price_list INTEGER,
    product_id INTEGER,
    priority INTEGER,
    price DOUBLE,
    currency VARCHAR(3),
    FOREIGN KEY (brand_id) REFERENCES BRAND(id)
);
