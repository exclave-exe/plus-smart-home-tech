
CREATE TABLE IF NOT EXISTS product_stock (
    product_id    UUID                PRIMARY KEY,
    width         DOUBLE PRECISION    NOT NULL,
    height        DOUBLE PRECISION    NOT NULL,
    depth         DOUBLE PRECISION    NOT NULL,
    weight        DOUBLE PRECISION    NOT NULL,
    fragile       BOOLEAN             NOT NULL,
    quantity      BIGINT              NOT NULL
);