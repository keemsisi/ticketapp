ALTER TABLE notification
ADD CONSTRAINT tbl_notification_col_workflow_id_fkey FOREIGN KEY (workflow_id)
REFERENCES workflow(id);