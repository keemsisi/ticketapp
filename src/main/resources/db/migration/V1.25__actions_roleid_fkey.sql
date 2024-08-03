ALTER TABLE action
ADD COLUMN IF NOT EXISTS role_id uuid;

ALTER TABLE action
DROP CONSTRAINT IF EXISTS tbl__action_role_id_fkey;

ALTER TABLE action
ADD CONSTRAINT tbl__action_role_id_fkey FOREIGN KEY (role_id)
REFERENCES role(id);
