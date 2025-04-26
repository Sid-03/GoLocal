-- Seed initial product data if the table is empty.
-- Uses ON CONFLICT to avoid errors if run multiple times with ddl-auto=update.

INSERT INTO product (name, price, image, supplier_name) VALUES
('Organic Apples', '$ 2.00 per lb', 'https://placehold.co/300x200/F87171/FFFFFF?text=Apples', 'Green Valley Orchards'),
('Fresh Carrots', '$ 1.50 per lb', 'https://placehold.co/300x200/FDBA74/FFFFFF?text=Carrots', 'Sunset Farms'),
('Organic Tomatoes', '$ 3.00 per lb', 'https://placehold.co/300x200/DC2626/FFFFFF?text=Tomatoes', 'Green Valley Orchards'),
('Free-Range Eggs', '$ 4.00 per dozen', 'https://placehold.co/300x200/FDE68A/FFFFFF?text=Eggs', 'Happy Hens Homestead'),
('Organic Spinach', '$ 2.50 per bunch', 'https://placehold.co/300x200/34D399/FFFFFF?text=Spinach', 'Sunset Farms'),
('Sweet Potatoes', '$ 1.80 per lb', 'https://placehold.co/300x200/F97316/FFFFFF?text=Sweet+Potato', 'Green Valley Orchards'),
('Artisan Sourdough Bread', '$ 6.00 per loaf', 'https://placehold.co/300x200/D1C4E9/FFFFFF?text=Bread', 'Local Bakehouse'),
('Local Honey (Wildflower)', '$ 8.50 per jar', 'https://placehold.co/300x200/FFECB3/FFFFFF?text=Honey', 'Happy Bee Apiary')
ON CONFLICT (name) DO UPDATE SET -- Example: Update price/image if name exists
    price = EXCLUDED.price,
    image = EXCLUDED.image,
    supplier_name = EXCLUDED.supplier_name;

-- If you prefer not to update existing ones, use:
-- ON CONFLICT (name) DO NOTHING;