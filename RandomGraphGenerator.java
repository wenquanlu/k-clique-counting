import java.util.Random;
public class RandomGraphGenerator {
    public int[][] generate(int dim) {
        int[][] m = new int[dim][dim];
        Random r = new Random();
        for (int i = 0; i < dim; i++) {
            m[i][i] = 0;
            for (int j = 0; j < i; j++) {
                m[i][j] = r.nextInt(2);
                m[j][i] = m[i][j];
            }
        }
        return m;
    }
}
