ALTER TABLE public.event
ADD COLUMN IF NOT EXISTS sub_categories json;

ALTER TABLE public.event
DROP COLUMN IF EXISTS event_category;

ALTER TABLE public.event
ALTER COLUMN IF EXISTS event_category DROP NOT NULL;