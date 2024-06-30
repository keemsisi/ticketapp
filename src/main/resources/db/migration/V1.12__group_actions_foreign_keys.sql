ALTER TABLE group_action
DROP CONSTRAINT IF EXISTS tbl_groups_col_group_id_fkey;

ALTER TABLE group_action
ADD CONSTRAINT tbl_groups_col_group_id_fkey FOREIGN KEY (group_id)
REFERENCES groups(id);

ALTER TABLE group_action
DROP CONSTRAINT IF EXISTS tbl_action_col_action_id_fkey;

ALTER TABLE group_action
ADD CONSTRAINT tbl_action_col_action_id_fkey FOREIGN KEY (action_id)
REFERENCES action(id);