package adventofcode.commons;

/**
 *
 */
public final class AoCMath {

    /**
     *
     */
    public static long lcm(long[] numbers) {
        long lcm = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            lcm = lcm(lcm, numbers[i]);
        }
        return lcm;
    }

    /**
     *
     */
    public static long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     *
     */
    public static long gcd(long a, long b) {
        while (b != 0) {
            long t = a;
            a = b;
            b = t % b;
        }
        return a;
    }

    /**
     *
     */
    private AoCMath() {
    }
}
