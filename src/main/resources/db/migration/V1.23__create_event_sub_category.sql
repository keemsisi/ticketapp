INSERT INTO public.event_category (id,date_created,date_modified,deleted,modified_by,user_id,"version",description,"name") VALUES
	 ('29e6e488-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Arts and Culture','ARTS_AND_CULTURE'),
	 ('29e705ee-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Charity and Aid','CHARITY_AND_AID'),
	 ('29e70904-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Community Work','COMMUNITY_WORK'),
	 ('29e70abc-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Education and Career','EDUCATION_AND_CAREER'),
	 ('29e70d8c-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Festival','FESTIVAL'),
	 ('29e70f1c-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Government','GOVERNMENT'),
	 ('29e710fc-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Music and Performances','MUSIC_AND_PERFORMANCES'),
	 ('29e71278-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Ceremony','CEREMONY'),
	 ('29e713e0-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Sports and Fitness','SPORTS_AND_FITNESS'),
	 ('29e71570-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Food and Drinks','FOOD_AND_DRINKS') ON CONFLICT DO NOTHING;
INSERT INTO public.event_category (id,date_created,date_modified,deleted,modified_by,user_id,"version",description,"name") VALUES
	 ('29e71674-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Investments','INVESTMENTS'),
	 ('29e71782-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Media and Film','MEDIA_AND_FILM'),
	 ('29e71872-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Meet and Greet','MEET_AND_GREET'),
	 ('29e71976-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Startups and Small Businesses','STARTUPS_AND_SMALL_BUSINESSES'),
	 ('29e71a70-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Technology and Business','TECHNOLOGY_AND_BUSINESS'),
	 ('29e71b60-442f-11ef-9560-42010a400002','2024-07-17 00:00:00.000',NULL,false,NULL,NULL,0,'Weekend Party','WEEKEND_PARTY'),
	 ('f70e3f5a-0980-4cf1-9137-9fcf497e3431','2024-07-19 15:41:32.204',NULL,false,NULL,'258d4be8-129d-42d9-b3e9-634fda834dcb',0,'Just testing a category','ADESHINA')
	 ON CONFLICT DO NOTHING;
