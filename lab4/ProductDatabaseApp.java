import java.sql.SQLException;

public class ProductDatabaseApp {
    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();

        try {
            repository.initialize();
            ProductShowcaseSwing.showUi(repository.findAll());
        } catch (SQLException e) {
            System.err.println("Không thể khởi tạo CSDL: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
