import java.util.HashMap;
import java.util.List;

public class Runner {

    public static void printAdjMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Graph g = new Graph();
        //RandomGraphGenerator generator = new RandomGraphGenerator();
        //int[][] matrix = generator.generate(6);
        int[][] matrix = new int[][] 
            {{0, 1, 1, 0, 1, 1},
            {1, 0, 1, 1, 1, 0}, 
            {1, 1, 0, 1, 1, 1},
            {0, 1, 1, 0, 1, 1}, 
            {1, 1, 1, 1, 0, 1}, 
            {1, 0, 1, 1, 1, 0}};
        printAdjMatrix(matrix);
        g.constructFromMatrix(matrix, 6);
        g.printUndirectedGraph();
        g.greedyColor(6, new int[] {0,1,2,3,4,5});
        g.printColors();
        g.colorOrdering().get(0);
        g.constructDAG();
        g.printDAG();
        int[][][] H = g.DPTriangularPathCount(3);
        /*for (int i = 0; i < H.length; i++) {
            for (int j = 0; j < H[0].length; j++) {
                for (int k = 1; k < H[0][0].length; k++) {
                    if (i != j) {
                        System.out.println("(" + i + ", " + j + ", " + k + "): " + H[i][j][k]);
                    }
                }
                //System.out.println("(" + i + ", " + j + ", " + (H[0][0].length-1) + "): " + H[i][j][H[0][0].length-1]);
            } 
        }*/
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            List<Node> path = g.DPTriangularPathSampling(H, 3);
            int hash = path.get(0).index * 100 + 
            path.get(1).index * 10 + path.get(2).index;
            if (map.containsKey(hash)) {
                map.put(hash, map.get(hash)+1);
            } else {
                map.put(hash, 1);
            }
        }
        
        System.out.println(map);

        /* 
        int zeros = 0;
        int ones = 0;
        int twos = 0;
        for (int i = 0; i < 100000; i++) {
            int x = g.sampleWithProbability(new double[] {0.2,0.3,0.5,0,0,0}, g.nodes).index;
            if (x == 0) {
                zeros += 1;
            }
            if (x == 1) {
                ones += 1;
            }
            if (x == 2) {
                twos += 1;
            }
        }
        System.out.println(zeros + " " + ones + " " + twos);
        */

    }

}
