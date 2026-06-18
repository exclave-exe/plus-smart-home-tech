
CREATE TABLE IF NOT EXISTS product_stock (
    product_id    UUID                PRIMARY KEY,
    width         DOUBLE PRECISION    NOT NULL,
    height        DOUBLE PRECISION    NOT NULL,
    depth         DOUBLE PRECISION    NOT NULL,
    weight        DOUBLE PRECISION    NOT NULL,
    fragile       BOOLEAN             NOT NULL,
    quantity      BIGINT              NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
                          booking_id UUID PRIMARY KEY,
                          fragile BOOLEAN NOT NULL,
                          delivery_volume DOUBLE PRECISION NOT NULL,
                          delivery_weight DOUBLE PRECISION NOT NULL,
                          delivery_id UUID,
                          order_id UUID
);

CREATE TABLE IF NOT EXISTS booking_products (
                                  booking_id UUID NOT NULL,
                                  product_id UUID NOT NULL,
                                  quantity BIGINT NOT NULL,
                                  PRIMARY KEY (booking_id, product_id),
                                  FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);