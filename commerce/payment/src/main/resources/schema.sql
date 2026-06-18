CREATE TABLE IF NOT EXISTS payment (
                         payment_id UUID PRIMARY KEY,
                         order_id UUID,
                         total_payment DECIMAL(19,2),
                         delivery_total DECIMAL(19,2),
                         fee_total DECIMAL(19,2),
                         payment_state VARCHAR(50) NOT NULL
);