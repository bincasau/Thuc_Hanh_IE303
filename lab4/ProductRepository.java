import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "3307");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "lab4_products");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");
    private static final String SERVER_URL = String.format(
            "jdbc:mysql://%s:%s/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8",
            DB_HOST,
            DB_PORT
    );
    private static final String DB_URL = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8",
            DB_HOST,
            DB_PORT,
            DB_NAME
    );

    public void initialize() throws SQLException {
        // loadDriver(); // Không cần thiết ở các phiên bản JDBC mới (>= 4.0)
        createDatabaseIfNeeded();
        createTableIfNeeded();
        seedDataIfNeeded();
    }

    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, brand, price, description, image_path FROM products ORDER BY id";
        return query(sql);
    }

    public List<Product> findByBrand(String brand) throws SQLException {
        String sql = "SELECT id, name, brand, price, description, image_path " +
                "FROM products WHERE LOWER(brand) = LOWER(?) ORDER BY price DESC, id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, brand);
            return executeQuery(statement);
        }
    }

    public List<Product> searchByKeyword(String keyword) throws SQLException {
        String sql = "SELECT id, name, brand, price, description, image_path " +
                "FROM products " +
                "WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?) " +
                "ORDER BY id";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            statement.setString(1, like);
            statement.setString(2, like);
            return executeQuery(statement);
        }
    }

    private void createTableIfNeeded() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS products (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(120) NOT NULL,
                    brand VARCHAR(80) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    description VARCHAR(500) NOT NULL,
                    image_path VARCHAR(255)
                )
                """;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void createDatabaseIfNeeded() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        try (Connection connection = getServerConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void seedDataIfNeeded() throws SQLException {
        if (countProducts() > 0) {
            return;
        }

        String sql = "INSERT INTO products (name, brand, price, description, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Product product : sampleProducts()) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getBrand());
                statement.setDouble(3, product.getPrice());
                statement.setString(4, product.getDescription());
                statement.setString(5, product.getImagePath());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private int countProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private List<Product> query(String sql) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return executeQuery(statement);
        }
    }

    private List<Product> executeQuery(PreparedStatement statement) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }
        }
        return products;
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("brand"),
                resultSet.getDouble("price"),
                resultSet.getString("description"),
                resultSet.getString("image_path")
        );
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASSWORD);
    }

    // Không cần dùng cái này nữa nếu chạy JDBC 4.0 trở lên, nhưng vẫn cần add thư viện nha
    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Chua tim thay MySQL JDBC driver (mysql-connector-j).", e);
        }
    }

    private List<Product> sampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("4DFWD PULSE SHOES", "Adidas", 160.00,
                "This product is excluded from all promotional discounts and offers.",
                "img1.png"));
        products.add(new Product("FORUM MID SHOES", "Adidas", 100.00,
                "Classic mid-top style with bold blue accents for everyday streetwear.",
                "img2.png"));
        products.add(new Product("SUPERNOVA SHOES", "Adidas", 150.00,
                "Responsive cushioning and breathable mesh for smooth daily runs.",
                "img3.png"));
        products.add(new Product("NMD City Stock 2", "Adidas", 160.00,
                "Modern 4D cushioning paired with a crisp upper and neon details.",
                "img4.png"));
        products.add(new Product("NMD City Stock 2", "Adidas", 120.00,
                "Stealth black finish with subtle violet accents and premium comfort.",
                "img5.png"));
        products.add(new Product("4DFWD Pulse Run", "Adidas", 160.00,
                "Lightweight performance runner with bright coral energy return.",
                "img6.png"));
        products.add(new Product("4DFWD Pulse Shoes", "Adidas", 160.00,
                "Signature 4D midsole and sleek knit upper built for standout comfort.",
                "img1.png"));
        products.add(new Product("Forum Mid Shoes", "Adidas", 100.00,
                "Retro basketball silhouette reimagined for a fresh casual outfit.",
                "img2.png"));
        return products;
    }
}