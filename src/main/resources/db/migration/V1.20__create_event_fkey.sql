ALTER TABLE event
DROP CONSTRAINT IF EXISTS tbl_event_col_user_id_id_fkey;

ALTER TABLE event
ADD CONSTRAINT tbl_groups_col_group_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);