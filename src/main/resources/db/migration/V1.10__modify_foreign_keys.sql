ALTER TABLE action
DROP CONSTRAINT IF EXISTS tbl_module_col_module_id_fkey;

ALTER TABLE action
ADD CONSTRAINT tbl_module_col_module_id_fkey FOREIGN KEY (module_id)
REFERENCES module(id);