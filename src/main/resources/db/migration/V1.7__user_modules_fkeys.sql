ALTER TABLE user_module
DROP CONSTRAINT IF EXISTS tbl_module_col_module_id_fkey;

ALTER TABLE user_module
ADD CONSTRAINT tbl_module_col_module_id_fkey FOREIGN KEY (module_id)
REFERENCES module(id);

ALTER TABLE user_module
DROP CONSTRAINT IF EXISTS tbl_users_col_user_id_fkey;

ALTER TABLE user_module
ADD CONSTRAINT tbl_users_col_user_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);
