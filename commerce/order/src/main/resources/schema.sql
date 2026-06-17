CREATE TABLE IF NOT EXISTS "order" (
                         order_id UUID PRIMARY KEY,
                         username VARCHAR(255) NOT NULL,
                         shopping_cart_id UUID,
                         payment_id UUID,
                         delivery_id UUID,
                         state VARCHAR(50) NOT NULL,
                         delivery_weight DOUBLE PRECISION,
                         delivery_volume DOUBLE PRECISION,
                         fragile BOOLEAN,
                         total_price DECIMAL(19,2),
                         delivery_price DECIMAL(19,2),
                         product_price DECIMAL(19,2)
);

CREATE TABLE IF NOT EXISTS order_products (
                                order_id UUID NOT NULL,
                                product_id UUID NOT NULL,
                                quantity INTEGER NOT NULL,
                                PRIMARY KEY (order_id, product_id),
                                FOREIGN KEY (order_id) REFERENCES "order"(order_id) ON DELETE CASCADE
);