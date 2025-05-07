CREATE SEQUENCE account_number_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE FUNCTION generate_account_number()
RETURNS VARCHAR(10)
AS $$
BEGIN
    RETURN '65578' || LPAD(NEXTVAL('account_number_seq')::TEXT, 5, '0');
END;
$$ LANGUAGE plpgsql;