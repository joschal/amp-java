import org.junit.Test;

public class BinCapacityCalculator {


    @Test
    public void binCapacityCalculator() {

        for (int m = 3; m <= 64; m++) {

            int u = getUforM(m);
            int Se = getSe(m, u);
            System.out.println("m=" + m + " --> u=" + u + " --> Se=" + Se);

        }

    }

    public int getUforM(int m) {

        for (int u = 1; u < 1024; u++) {

            double a = ((2 * (m - u)) / log2(m)) + 2;

            double b = Math.pow(2, u);

            if (a <= b) {
                return u;
            }

        }

        throw new RuntimeException("No u found for m=" + m);

    }

    public int getSe(int m, int u) {

        int ceil = (int) Math.ceil((m - u) / log2(m));

        assert (ceil > 0);

        return 2 * ceil;

    }

    public static double log2(int x) {
        return (Math.log(x) / Math.log(2));
    }

}
