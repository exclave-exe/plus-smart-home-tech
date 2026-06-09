CREATE TABLE IF NOT EXISTS cart (
    cart_id     UUID            PRIMARY KEY,
    username    VARCHAR(255)    NOT NULL,
    active      BOOLEAN         NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id       UUID    NOT NULL,
    product_id    UUID    NOT NULL,
    quantity      INTEGER,

    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id)
);