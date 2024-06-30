ALTER TABLE workflow_level
ADD COLUMN IF NOT EXISTS approval_status workflow_approval_status_enum DEFAULT 'PENDING';

ALTER TABLE workflow_level
ADD COLUMN IF NOT EXISTS approved_by uuid;

ALTER TABLE workflow_level
ADD COLUMN IF NOT EXISTS remarks text;

ALTER TABLE workflow_level
ADD COLUMN IF NOT EXISTS approval_date timestamptz default NULL;