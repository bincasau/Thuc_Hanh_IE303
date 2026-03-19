import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bai4 {

    public static List<Integer> best = new ArrayList<>();

    public static void backtrack(int index, int[] arr, int k, int sum, List<Integer> newarr) {
        if (sum == k) {
            if (newarr.size() > best.size()) {
                best = new ArrayList<>(newarr);
            }
            return;
        }

        if (sum > k || index >= arr.length) {
            return;
        }

        newarr.add(arr[index]);
        backtrack(index + 1, arr, k, sum + arr[index], newarr);
        newarr.remove(newarr.size() - 1);
        backtrack(index + 1, arr, k, sum, newarr);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhap n va K:");
        int n = sc.nextInt();
        int k = sc.nextInt();

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        backtrack(0, arr, k, 0, new ArrayList<>());

        System.out.println("Day dai nhat co tong = K:");
        System.out.println(best);
    }
}