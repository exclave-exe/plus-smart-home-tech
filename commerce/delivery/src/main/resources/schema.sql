CREATE TABLE IF NOT EXISTS address (
                         address_id UUID PRIMARY KEY,
                         country VARCHAR(255),
                         city VARCHAR(255),
                         street VARCHAR(255),
                         house VARCHAR(255),
                         flat VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS delivery (
                          delivery_id UUID PRIMARY KEY,
                          from_address_id UUID,
                          to_address_id UUID,
                          order_id UUID,
                          delivery_state VARCHAR(50) NOT NULL,
                          FOREIGN KEY (from_address_id) REFERENCES address(address_id),
                          FOREIGN KEY (to_address_id) REFERENCES address(address_id)
);