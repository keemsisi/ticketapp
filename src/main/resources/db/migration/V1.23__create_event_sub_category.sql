INSERT INTO public.event_category (name, description)
VALUES
    ('Arts and Culture', 'Arts and Culture'),
    ('Charity and Aid', 'Charity and Aid'),
    ('Community Work', 'Community Work'),
    ('Education and Career', 'Education and Career'),
    ('Festival', 'Festival'),
    ('Government', 'Government'),
    ('Music and Performances', 'Music and Performances'),
    ('Ceremony', 'Ceremony'),
    ('Sports and Fitness', 'Sports and Fitness'),
    ('Food and Drinks', 'Food and Drinks'),
    ('Investments', 'Investments'),
    ('Media and Film', 'Media and Film'),
    ('Meet and Greet', 'Meet and Greet'),
    ('Startups and Small Businesses', 'Startups and Small Businesses'),
    ('Technology and Business', 'Technology and Business'),
    ('Weekend Party', 'Weekend Party')
ON CONFLICT DO NOTHING;
