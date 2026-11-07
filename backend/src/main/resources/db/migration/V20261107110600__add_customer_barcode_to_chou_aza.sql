-- Add customer_barcode column to chou_aza table
-- This column stores the customer barcode value for postal mail addressing
-- Format: Start code(1) + Postal code(7) + Address number(13) + Check digit(1) + Stop code(1) = 23 characters

ALTER TABLE undecided_ef.chou_aza
    ADD COLUMN customer_barcode VARCHAR(23) COMMENT 'Customer barcode value';
