ALTER TABLE user_role
DROP CONSTRAINT IF EXISTS tbl_role_col_role_id_fkey;

ALTER TABLE user_role
ADD CONSTRAINT tbl_role_col_role_id_fkey FOREIGN KEY (role_id)
REFERENCES role(id);

ALTER TABLE user_role
DROP CONSTRAINT IF EXISTS tbl_user_col_user_id_fkey;

ALTER TABLE user_role
ADD CONSTRAINT tbl_user_col_user_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);