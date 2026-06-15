CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    brand VARCHAR(80) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image_path VARCHAR(255)
);

INSERT INTO products (name, brand, price, description, image_path) VALUES
('4DFWD PULSE SHOES', 'Adidas', 160.00, 'This product is excluded from all promotional discounts and offers.', 'img1.png'),
('FORUM MID SHOES', 'Adidas', 100.00, 'Classic mid-top style with bold blue accents for everyday streetwear.', 'img2.png'),
('SUPERNOVA SHOES', 'Adidas', 150.00, 'Responsive cushioning and breathable mesh for smooth daily runs.', 'img3.png'),
('NMD City Stock 2', 'Adidas', 160.00, 'Modern 4D cushioning paired with a crisp upper and neon details.', 'img4.png'),
('NMD City Stock 2', 'Adidas', 120.00, 'Stealth black finish with subtle violet accents and premium comfort.', 'img5.png'),
('4DFWD Pulse Run', 'Adidas', 160.00, 'Lightweight performance runner with bright coral energy return.', 'img6.png'),
('4DFWD Pulse Shoes', 'Adidas', 160.00, 'Signature 4D midsole and sleek knit upper built for standout comfort.', 'img1.png'),
('Forum Mid Shoes', 'Adidas', 100.00, 'Retro basketball silhouette reimagined for a fresh casual outfit.', 'img2.png');
