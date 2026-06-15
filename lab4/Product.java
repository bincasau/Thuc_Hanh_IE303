import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private static final NumberFormat PRICE_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    private final int id;
    private final String name;
    private final String brand;
    private final double price;
    private final String description;
    private final String imagePath;

    public Product(int id, String name, String brand, double price, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Product(String name, String brand, double price, String description, String imagePath) {
        this(0, name, brand, price, description, imagePath);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String toTableRow() {
        return String.format(
                "%-3d %-24s %-10s %-10s %-40s",
                id,
                trim(name, 24),
                trim(brand, 10),
                PRICE_FORMAT.format(price),
                trim(description, 40)
        );
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + PRICE_FORMAT.format(price) +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    private static String trim(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
