INSERT INTO public."role"
(id, code, created_by, created_on, description, is_deleted, modified_by, modified_on, "name", normalized_name)
VALUES('a7f0a602-719f-4e72-943a-51ea2014a158'::uuid, 'super_admin', '258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid, '2021-12-07 10:55:22.432', 'Super Administrator', false, NULL, NULL, 'super admin', 'SUPERADMIN')
ON CONFLICT DO NOTHING;


INSERT INTO public.user_role
(id, created_by, created_on, expiry_date, is_deleted, modified_by, modified_on, role_id, user_id)
VALUES('246c1706-8a71-450b-928d-e181f864da21'::uuid, '258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid, '2024-02-19 08:00:49.100', NULL, false, NULL, NULL, 'a7f0a602-719f-4e72-943a-51ea2014a158'::uuid, '258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid)
ON CONFLICT DO NOTHING;
