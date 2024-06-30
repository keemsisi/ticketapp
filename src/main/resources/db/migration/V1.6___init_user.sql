INSERT INTO public.users
(id, address, city, country, created_by, created_on, deactivated, deleted
, dob, email, first_name, first_time_login, gender, job_description,
last_failed_login, last_login, last_name, level_id, lga_of_origin, lock_date,
"locked", locked_by, login_attempt, marital_status, middle_name, modified_by,
modified_on, occupation, "password", password_created_on, phone, postal_code,
profile_picture_location, state_of_origin, password_expiry_date)
VALUES('258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid, 'Ilupeju', 'Lagos',
'Nigeria', '454a4983-aa86-4a51-b6e8-3efb450bcffd'::uuid, '2022-09-19 15:47:13.150',
false, false, '2022-09-19 15:47:13.150',
'regtmpuser4@africaprudential.com', 'System', false, 'OTHERS', 'System Admin',
'2024-06-24 18:02:53.123', '2024-06-28 17:32:49.222', 'Administrator', NULL, 'Ilupeju',
 NULL, false, '454a4983-aa86-4a51-b6e8-3efb450bcffd'::uuid, 0, '', '',
 '258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid, '2023-12-10 12:53:43.651', '',
 '$2a$10$nYdn1mGteyz0ZIOPXVrFF.6r2PtsrTlaim.t.MLBbjNZC/jOBFO96', '2023-12-10 12:53:43.651',
 '09030863117', '', '', '', NULL) ON CONFLICT DO NOTHING;