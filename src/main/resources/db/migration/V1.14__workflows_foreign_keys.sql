ALTER TABLE workflow
DROP CONSTRAINT IF EXISTS tbl_tenant_col_tenant_id_fkey;

ALTER TABLE workflow
ADD CONSTRAINT tbl_tenant_col_tenant_id_fkey FOREIGN KEY (tenant_id)
REFERENCES tenant(id);

ALTER TABLE workflow
DROP CONSTRAINT IF EXISTS tbl_action_col_action_id_fkey;

ALTER TABLE workflow
ADD CONSTRAINT tbl_action_col_action_id_fkey FOREIGN KEY (action_id)
REFERENCES action(id);

ALTER TABLE workflow_level
DROP CONSTRAINT IF EXISTS tbl_workflow_col_workflow_id_fkey;

ALTER TABLE workflow_level
ADD CONSTRAINT tbl_workflow_col_workflow_id_fkey FOREIGN KEY (workflow_id)
REFERENCES workflow(id);

ALTER TABLE workflow_level
DROP CONSTRAINT IF EXISTS tbl_role_col_role_id_fkey;

ALTER TABLE workflow_level
ADD CONSTRAINT tbl_role_col_role_id_fkey FOREIGN KEY (role_id)
REFERENCES role(id);