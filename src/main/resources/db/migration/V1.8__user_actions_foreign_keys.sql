ALTER TABLE user_action
DROP CONSTRAINT IF EXISTS tbl_action_col_action_id_fkey;

ALTER TABLE user_action
ADD CONSTRAINT tbl_action_col_action_id_fkey FOREIGN KEY (action_id)
REFERENCES action(id);

ALTER TABLE user_action
DROP CONSTRAINT IF EXISTS tbl_users_col_user_id_fkey;

ALTER TABLE user_action
ADD CONSTRAINT tbl_users_col_user_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);