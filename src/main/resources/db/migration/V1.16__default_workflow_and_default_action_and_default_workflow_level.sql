-- create default role
INSERT INTO role
(id, code, created_by, created_on, description, is_deleted, modified_by, modified_on, "name", normalized_name)
VALUES('75d98a27-ed55-4bee-b385-e8e9e8600060'::uuid, 'role.default', '8b48d8c2-38ff-4f44-957b-47bdad8988d8'::uuid, CURRENT_TIMESTAMP, 'default role', false, NULL, NULL, 'default role', 'defaultrole')
ON CONFLICT DO NOTHING;