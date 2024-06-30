ALTER TABLE group_user
DROP CONSTRAINT IF EXISTS tbl_group_col_group_id_fkey;

ALTER TABLE group_user
ADD CONSTRAINT tbl_group_col_group_id_fkey FOREIGN KEY (group_id)
REFERENCES groups(id);

ALTER TABLE group_user
DROP CONSTRAINT IF EXISTS tbl_user_col_user_id_fkey;

ALTER TABLE group_user
ADD CONSTRAINT tbl_user_col_user_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);