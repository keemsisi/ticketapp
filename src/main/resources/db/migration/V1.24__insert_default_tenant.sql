INSERT INTO tenant (id,account_lockout_duration_in_minutes,account_lockout_threshold_count,address,country,
created_by,created_on,currency,deleted,email,inactive_period_in_minutes,logo_location,
modified_by,modified_on,"name",normalized_name,password_expiration_in_days,
phone,state,system_alert_id,two_fa_enabled) VALUES
('ced059a5-4dc7-4d05-ae1f-e415a7f4677e',60,5,'22, Ishadare street Mushin Lagos','NGA',NULL,
NULL,'NGN',false,'code@keemsisi.com',15,'https://test.blob.core.windows.net/test.png',NULL,
NULL,'Ticket App Tenant','TICKETAPPTENANT',300,'08011143111','Lagos','4db51f4b-3df7-4b86-ab95-5cec47e626c4',false)
ON CONFLICT DO NOTHING;

INSERT INTO system_alert
(id, created_by, date_created, date_modified, email_alert, modified_by, sms_alert, tenant_id)
VALUES('4db51f4b-3df7-4b86-ab95-5cec47e626c4'::uuid, '258d4be8-129d-42d9-b3e9-634fda834dcb'::uuid, '2021-11-27 21:31:34.128', '2021-12-10 10:38:20.504',
true, 'bb7470b5-0a69-4567-9e19-de1b89a7c380'::uuid, false, 'ced059a5-4dc7-4d05-ae1f-e415a7f4677e'::uuid)
ON CONFLICT DO NOTHING;