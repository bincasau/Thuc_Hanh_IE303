import java.util.*;

public class Bai3 {

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int cross(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y)
                - (a.y - o.y) * (b.x - o.x);
    }

    static int dist2(Point a, Point b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    static List<Point> grahamScan(List<Point> pts) {
        int n = pts.size();
        if (n <= 1) return pts;

        Point p0 = pts.get(0);
        for (Point p : pts) {
            if (p.y < p0.y || (p.y == p0.y && p.x < p0.x)) {
                p0 = p;
            }
        }

        Point finalP0 = p0;

        pts.sort((a, b) -> {
            int c = cross(finalP0, a, b);
            if (c == 0) return dist2(finalP0, a) - dist2(finalP0, b);
            return -c;
        });

        Stack<Point> st = new Stack<>();
        st.push(pts.get(0));
        st.push(pts.get(1));

        for (int i = 2; i < n; i++) {
            while (st.size() >= 2) {
                Point top = st.pop();
                Point next = st.peek();

                if (cross(next, top, pts.get(i)) > 0) {
                    st.push(top);
                    break;
                }
            }
            st.push(pts.get(i));
        }

        return new ArrayList<>(st);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập vào số lượng điểm: ");
        int n = sc.nextInt();

        List<Point> arr = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            arr.add(new Point(x, y));
        }

        List<Point> hull = grahamScan(arr);

        System.out.println("Bao lồi:");
        for (Point p : hull) {
            System.out.println(p.x + " " + p.y);
        }
    }
}