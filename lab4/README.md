# Lab 4

Xây dựng CSDL lưu thông tin sản phẩm bằng MySQL và hiển thị lại giao diện bài lab3 bằng Java Swing + JDBC.

## Nội dung

- `pom.xml`: File cấu hình Maven để quản lý thư viện (dependency), bao gồm cả MySQL Connector/J.
- `schema.sql`: câu lệnh tạo bảng `products` và dữ liệu mẫu.
- `Product.java`: model sản phẩm.
- `ProductRepository.java`: thao tác tạo database, tạo bảng, thêm dữ liệu mẫu và truy vấn.
- `ProductShowcaseSwing.java`: giao diện hiển thị sản phẩm giống lab3, nhưng lấy dữ liệu từ CSDL.
- `ProductDatabaseApp.java`: chương trình khởi tạo CSDL và mở giao diện.

## Chạy trong IntelliJ

Dự án này được quản lý bằng Maven.

1. Mở thư mục `lab4` trong IntelliJ.
2. IntelliJ sẽ tự động nhận diện file `pom.xml`. Nếu không, hãy mở thanh công cụ Maven (View -> Tool Windows -> Maven) và nhấn nút "Reload All Maven Projects".
3. Maven sẽ tự động tải về thư viện MySQL Connector/J cần thiết.
4. Chạy file `ProductDatabaseApp.java`.

## Cấu hình MySQL

Mặc định chương trình dùng:

- host: `localhost`
- port: `3307`
- database: `lab4_products`
- user: `root`
- password: rỗng

Nếu máy khác cấu hình này, đặt biến môi trường trước khi chạy:

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "3307"
$env:DB_NAME = "lab4_products"
$env:DB_USER = "root"
$env:DB_PASSWORD = ""
```

## Kết quả

Chương trình sẽ:

1. Kết nối tới MySQL server.
2. Tự tạo database `lab4_products` nếu chưa có.
3. Tự tạo bảng `products` nếu chưa có.
4. Tự nạp dữ liệu mẫu từ bài thực hành 03 nếu bảng rỗng.
5. Mở giao diện giống lab3:
   - danh sách sản phẩm ở bên phải,
   - khung chi tiết ở bên trái,
   - click vào sản phẩm để đổi nội dung và có hiệu ứng chuyển.
