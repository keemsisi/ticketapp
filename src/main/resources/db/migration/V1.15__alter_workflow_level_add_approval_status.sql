ALTER TABLE workflow_level
ADD COLUMN approval_status workflow_approval_status_enum DEFAULT 'PENDING';

ALTER TABLE workflow_level
ADD COLUMN approved_by uuid;

ALTER TABLE workflow_level
ADD COLUMN remarks text;

ALTER TABLE workflow_level
ADD COLUMN approval_date timestamptz default NULL;