ALTER TABLE event
ADD CONSTRAINT tbl_event_col_user_id_id_fkey FOREIGN KEY (user_id)
REFERENCES user(id);