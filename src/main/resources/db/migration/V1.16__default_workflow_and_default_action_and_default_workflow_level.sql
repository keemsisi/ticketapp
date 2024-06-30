
-- create default role
INSERT INTO role
(id, code, created_by, created_on, description, is_deleted, modified_by, modified_on, "name", normalized_name)
VALUES('75d98a27-ed55-4bee-b385-e8e9e8600060'::uuid, 'role.default', '8b48d8c2-38ff-4f44-957b-47bdad8988d8'::uuid, CURRENT_TIMESTAMP, 'default role', false, NULL, NULL, 'default role', 'defaultrole');
-- Create default action

INSERT INTO workflow(id, name, action_id, normalized_name,active, description ,created_by , modified_by , created_on , modified_on , is_deleted , module_id)
VALUES ('2c5f4f72-48b0-4f4d-96e4-ba2eae738609'::uuid, 'DEFAULT_WORKFLOW',
'7d837372-1891-48c1-bc60-5b8a46655d8b'::uuid, 'DEFAULT_WORKFLOW', true ,
'This workflow should be used when there is no workflow available int the table',
'8b48d8c2-38ff-4f44-957b-47bdad8988d8'::uuid , NULL , CURRENT_TIMESTAMP, NULL , FALSE, '12a46804-5653-4b81-bdf4-87f5d259abdc'::uuid);

-- Create default level
INSERT INTO workflow_level(id, workflow_id , role_id , name , normalized_name, description , approval_status,  approved_by , remarks , approval_date , level_no , created_on , modified_on ,created_by , modified_by , is_deleted )
VALUES ('c571004d-5da2-48cc-8a00-e5d3fc2d03cc'::uuid ,'2c5f4f72-48b0-4f4d-96e4-ba2eae738609'::uuid, '75d98a27-ed55-4bee-b385-e8e9e8600060'::uuid, 'DEFAULT WORKFLOW LEVEL', 'DEFAULT WORKFLOW LEVEL', 'Default workflow Level',
'PENDING'::workflow_approval_status_enum, NULL, NULL, NULL, 1, CURRENT_TIMESTAMP,NULL, '8b48d8c2-38ff-4f44-957b-47bdad8988d8'::uuid, NULL, FALSE);