ALTER TABLE event_seat_sections
DROP CONSTRAINT IF EXISTS tbl__event_seat_sections_user_id_fkey;

ALTER TABLE event_seat_sections
ADD CONSTRAINT tbl__event_seat_sections_user_id_fkey FOREIGN KEY (user_id)
REFERENCES users(id);

ALTER TABLE event_seat_sections
DROP CONSTRAINT IF EXISTS tbl__event_seat_sections_event_id_fkey;

ALTER TABLE event_seat_sections
ADD CONSTRAINT tbl__event_seat_sections_event_id_fkey FOREIGN KEY (event_id)
REFERENCES event(id);