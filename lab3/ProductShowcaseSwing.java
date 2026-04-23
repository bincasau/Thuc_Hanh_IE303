import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProductShowcaseSwing extends JFrame {
    private final List<ProductCard> cards = new ArrayList<>();
    private final ProductDetailPanel detailPanel = new ProductDetailPanel();

    public ProductShowcaseSwing() {
        super("Adidas Product Showcase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 720));
        setPreferredSize(new Dimension(1280, 760));
        setContentPane(buildContent());
        pack();
        setLocationRelativeTo(null);

        if (!cards.isEmpty()) {
            selectProduct(cards.get(0).getProduct(), false);
        }
    }

    private JPanel buildContent() {
        List<Product> products = buildProducts();

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(14, 18, 18, 18));

        detailPanel.setPreferredSize(new Dimension(310, 0));

        JPanel grid = new JPanel(new java.awt.GridLayout(0, 4, 12, 12));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        for (Product product : products) {
            ProductCard card = new ProductCard(product);
            MouseAdapter selectionHandler = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectProduct(product, true);
                }
            };
            installSelectionHandler(card, selectionHandler);
            cards.add(card);
            grid.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);

        JPanel center = new JPanel(new BorderLayout(22, 0));
        center.setOpaque(false);
        center.add(detailPanel, BorderLayout.WEST);
        center.add(scrollPane, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private void installSelectionHandler(Component component, MouseAdapter handler) {
        component.addMouseListener(handler);
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                installSelectionHandler(child, handler);
            }
        }
    }

    private void selectProduct(Product product, boolean animate) {
        for (ProductCard card : cards) {
            card.setSelected(card.getProduct() == product);
        }
        detailPanel.showProduct(product, animate);
    }

    private List<Product> buildProducts() {
        return Arrays.asList(
                new Product("4DFWD PULSE SHOES", "Adidas", 160.00,
                        "This product is excluded from all promotional discounts and offers.",
                        loadImage("img1.png")),
                new Product("FORUM MID SHOES", "Adidas", 100.00,
                        "Classic mid-top style with bold blue accents for everyday streetwear.",
                        loadImage("img2.png")),
                new Product("SUPERNOVA SHOES", "Adidas", 150.00,
                        "Responsive cushioning and breathable mesh for smooth daily runs.",
                        loadImage("img3.png")),
                new Product("NMD City Stock 2", "Adidas", 160.00,
                        "Modern 4D cushioning paired with a crisp upper and neon details.",
                        loadImage("img4.png")),
                new Product("NMD City Stock 2", "Adidas", 120.00,
                        "Stealth black finish with subtle violet accents and premium comfort.",
                        loadImage("img5.png")),
                new Product("4DFWD Pulse Run", "Adidas", 160.00,
                        "Lightweight performance runner with bright coral energy return.",
                        loadImage("img6.png")),
                new Product("4DFWD Pulse Shoes", "Adidas", 160.00,
                        "Signature 4D midsole and sleek knit upper built for standout comfort.",
                        loadImage("img1.png")),
                new Product("Forum Mid Shoes", "Adidas", 100.00,
                        "Retro basketball silhouette reimagined for a fresh casual outfit.",
                        loadImage("img2.png"))
        );
    }

    private BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (IOException e) {
            BufferedImage fallback = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = fallback.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(0, 0, 600, 600, 42, 42);
            g2.setColor(new Color(180, 180, 180));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
            drawCenteredText(g2, "Missing image", 0, 0, 600, 600);
            g2.dispose();
            return fallback;
        }
    }

    private static void drawCenteredText(Graphics2D g2, String text, int x, int y, int width, int height) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new ProductShowcaseSwing().setVisible(true);
        });
    }

    private static class Product {
        private final String name;
        private final String brand;
        private final double price;
        private final String description;
        private final BufferedImage image;

        private Product(String name, String brand, double price, String description, BufferedImage image) {
            this.name = name;
            this.brand = brand;
            this.price = price;
            this.description = description;
            this.image = image;
        }
    }

    private static class ProductCard extends JPanel {
        private static final NumberFormat PRICE_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
        private final Product product;
        private boolean selected;

        private ProductCard(Product product) {
            this.product = product;
            setOpaque(false);
            setPreferredSize(new Dimension(210, 235));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setLayout(new BorderLayout(0, 0));

            JPanel content = new JPanel();
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(truncate(product.name, 16));
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(new Color(68, 68, 68));

            JLabel subtitleLabel = new JLabel(truncate(product.description, 23));
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            subtitleLabel.setForeground(new Color(190, 190, 190));

            JLabel imageLabel = new JLabel(new javax.swing.ImageIcon(scaleImage(product.image, 200, 200)));
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);
            footer.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel brandLabel = new JLabel(product.brand);
            brandLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            brandLabel.setForeground(new Color(90, 90, 90));

            JLabel priceLabel = new JLabel(PRICE_FORMAT.format(product.price));
            priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            priceLabel.setForeground(new Color(77, 77, 77));

            footer.add(brandLabel, BorderLayout.WEST);
            footer.add(priceLabel, BorderLayout.EAST);

            content.add(nameLabel);
            content.add(Box.createVerticalStrut(6));
            content.add(subtitleLabel);
            content.add(Box.createVerticalStrut(8));
            content.add(imageLabel);
            content.add(Box.createVerticalGlue());
            content.add(footer);

            add(content, BorderLayout.CENTER);
        }

        private Product getProduct() {
            return product;
        }

        private void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color background = selected ? new Color(248, 250, 255) : new Color(247, 247, 247);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1.0, getHeight() - 1.0, 18, 18));

            if (selected) {
                g2.setColor(new Color(86, 144, 255));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(0.75, 0.75, getWidth() - 1.5, getHeight() - 1.5, 18, 18));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ProductDetailPanel extends JPanel {
        private static final NumberFormat PRICE_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
        private Product currentProduct;
        private Product previousProduct;
        private float progress = 1f;
        private Timer animationTimer;

        private ProductDetailPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        }

        private void showProduct(Product nextProduct, boolean animate) {
            if (nextProduct == null) {
                return;
            }
            if (currentProduct == null || !animate) {
                currentProduct = nextProduct;
                previousProduct = null;
                progress = 1f;
                repaint();
                return;
            }
            if (currentProduct == nextProduct) {
                return;
            }
            previousProduct = currentProduct;
            currentProduct = nextProduct;
            progress = 0f;

            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            animationTimer = new Timer(16, e -> {
                progress += 0.08f;
                if (progress >= 1f) {
                    progress = 1f;
                    previousProduct = null;
                    animationTimer.stop();
                }
                repaint();
            });
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            if (previousProduct != null && progress < 1f) {
                paintProduct(g2, previousProduct, 1f - progress, Math.round(-28 * progress));
            }
            if (currentProduct != null) {
                float alpha = previousProduct == null ? 1f : progress;
                int offset = previousProduct == null ? 0 : Math.round(28 * (1f - progress));
                paintProduct(g2, currentProduct, alpha, offset);
            }
            g2.dispose();
        }

        private void paintProduct(Graphics2D g2, Product product, float alpha, int xOffset) {
            Graphics2D layer = (Graphics2D) g2.create();
            layer.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
            layer.translate(xOffset, 0);

            int width = getWidth();
            int imageAreaHeight = 240;
            Image scaledImage = scaleImage(product.image, width - 36, imageAreaHeight - 30);
            int imageX = (width - scaledImage.getWidth(null)) / 2;
            int imageY = 10 + (imageAreaHeight - scaledImage.getHeight(null)) / 2;
            layer.drawImage(scaledImage, imageX, imageY, null);

            int separatorY = imageAreaHeight + 22;
            layer.setColor(new Color(170, 170, 170));
            layer.drawLine(16, separatorY, width - 16, separatorY);

            int textY = separatorY + 40;
            layer.setColor(new Color(72, 72, 72));
            layer.setFont(new Font("Segoe UI", Font.BOLD, 18));
            drawWrappedText(layer, product.name, 16, textY, width - 32, 26, 2);

            layer.setFont(new Font("Segoe UI", Font.BOLD, 20));
            layer.drawString(PRICE_FORMAT.format(product.price), 16, textY + 48);

            layer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            layer.setColor(new Color(87, 87, 87));
            layer.drawString(product.brand, 16, textY + 72);

            layer.setFont(new Font("Segoe UI", Font.BOLD, 13));
            layer.setColor(new Color(165, 165, 165));
            drawWrappedText(layer, product.description, 16, textY + 98, width - 32, 20, 4);
            layer.dispose();
        }
    }

    private static void drawWrappedText(Graphics2D g2, String text, int x, int y, int width, int lineHeight, int maxLines) {
        FontMetrics metrics = g2.getFontMetrics();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        int lineCount = 0;
        int currentY = y;

        for (String word : words) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (metrics.stringWidth(candidate) <= width) {
                line.setLength(0);
                line.append(candidate);
            } else {
                g2.drawString(line.toString(), x, currentY);
                lineCount++;
                currentY += lineHeight;
                if (lineCount >= maxLines - 1) {
                    String ellipsis = fitWithEllipsis(g2, word, width);
                    g2.drawString(ellipsis, x, currentY);
                    return;
                }
                line.setLength(0);
                line.append(word);
            }
        }

        if (line.length() > 0 && lineCount < maxLines) {
            g2.drawString(line.toString(), x, currentY);
        }
    }

    private static String fitWithEllipsis(Graphics2D g2, String text, int width) {
        String value = text;
        FontMetrics metrics = g2.getFontMetrics();
        while (!value.isEmpty() && metrics.stringWidth(value + "...") > width) {
            value = value.substring(0, value.length() - 1);
        }
        return value + "...";
    }

    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private static Image scaleImage(BufferedImage image, int maxWidth, int maxHeight) {
        double scale = Math.min((double) maxWidth / image.getWidth(), (double) maxHeight / image.getHeight());
        scale = Math.min(scale, 1.0);
        int width = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(image.getHeight() * scale));
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
