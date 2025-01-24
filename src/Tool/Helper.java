package Tool;

import java.util.HashSet;

public class Helper {
    public static int PAIR_CODE(int x, int y) {
        int a = x >= 0 ? 2 * x : -2 * x - 1;
        int b = y >= 0 ? 2 * y : -2 * y - 1;

        return (a + b) * (a + b + 1) / 2 + b;
    }

    public static <T> HashSet<T> Intersect(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> result = new HashSet<>();
        for (T d : set1) {
            if (set2.contains(d)) result.add(d);
        }
        return result;
    }

    public static <T> HashSet<T> Minus(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> result = new HashSet<>();
        for (T d : set1) {
            if (!set2.contains(d)) result.add(d);
        }
        return result;
    }
}
