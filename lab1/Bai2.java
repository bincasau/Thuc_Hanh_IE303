import java.util.Scanner;

public class Bai2 {
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
        System.out.println("Bán kính hình tròn là 1:");
        double R = 1.0;
        int N = 1000000;
        System.out.println("Số PI: " + tinhDienTichHinhTron(R, N));
    }
}
