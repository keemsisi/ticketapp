ALTER TABLE action_workflow
DROP CONSTRAINT IF EXISTS tbl_workflow_col_workflow_id_fkey;

ALTER TABLE action_workflow
ADD CONSTRAINT IF NOT EXISTS tbl_workflow_col_workflow_id_fkey FOREIGN KEY (workflow_id)
REFERENCES workflow(id);

ALTER TABLE action_workflow
DROP CONSTRAINT IF EXISTS tbl_action_col_action_id_fkey;

ALTER TABLE action_workflow
ADD CONSTRAINT IF NOT EXISTS tbl_action_col_action_id_fkey FOREIGN KEY (action_id)
REFERENCES action(id);