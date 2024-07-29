ALTER TABLE event_wishlist
DROP CONSTRAINT IF EXISTS tbl__event_wishlist_event_id_fkey;

ALTER TABLE event_wishlist
ADD CONSTRAINT tbl__event_wishlist_event_id_fkey FOREIGN KEY (event_id)
REFERENCES event(id);