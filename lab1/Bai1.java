import java.util.Scanner;

public class Bai1 {
    public static double tinhDienTichHinhTron(double R, int soDiemN) {
        int soDiemTrongHinhTron = 0;

        for (int i = 0; i < soDiemN; i++) {
            double x = (Math.random() * 2 * R) - R;
            double y = (Math.random() * 2 * R) - R;
            if (x * x + y * y <= R * R) {
                soDiemTrongHinhTron++;
            }
        }

        double dienTichHinhVuong = 4 * R * R;
        return ((double) soDiemTrongHinhTron / soDiemN) * dienTichHinhVuong;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double R = sc.nextDouble();
        int N = 1000000;
        System.out.println("Diện tích hình tròn: " + tinhDienTichHinhTron(R, N));
    }
}
