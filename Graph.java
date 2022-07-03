import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Graph {
    List<Node> nodes = new ArrayList<>();

    public boolean addDirectedEdge(Node u, Node v) {
        u.add_out_neighbour(v);
        v.add_in_neighbour(u);
        return true;
    }

    public boolean addEdge(Node u, Node v) {
        return u.add_neighbour(v) && v.add_neighbour(u);
    }

    public void constructFromMatrix(int[][] matrix, int dim) {
        for (int i = 0; i < dim; i++) {
            Node n = new Node();
            n.index = i;
            nodes.add(n);
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < i; j++) {
                if (matrix[i][j] == 1) {
                    addEdge(nodes.get(i), nodes.get(j));
                }
            }
        }
    }

    public void printUndirectedGraph() {
        for (Node n: nodes) {
            System.out.print(n.index + ":");
            for (Node neighbour: n.neighbour) {
                System.out.print(" " + neighbour.index);
            }
            System.out.println();
        }
    }

    // color starts from 1
    public void greedyColor(int colors, int[] ordering) {
        for (int i: ordering) {
            int[] TraceColor = new int[colors];
            Node n = nodes.get(i);
            for (Node neighbour: n.neighbour) {
                if (neighbour.color > 0) {
                    TraceColor[neighbour.color - 1] = 1;
                }
            }

            boolean colored = false;
            for (int j = 0; j < colors; j++) {
                if (TraceColor[j] == 0) {
                    n.color = j+1;
                    colored = true;
                    break;
                }
            }
            if (colored == false) {
                System.out.println("Insufficient color");
            }
        }
    }

    public void printColors() {
        for (Node n: nodes) {
            System.out.println(n.index + ": " + n.color);
        }
    }

    public List<Node> colorOrdering() {
        List<Node> ordering = nodes.stream().sorted((n1, n2) -> n1.compareTo(n2)).collect(Collectors.toList());
        System.out.print("Color ordering: ");
        for (Node n: ordering) {
            System.out.print(" " + n.index);
        }
        System.out.println();
        return ordering;
    }

    public void constructDAG() {

        for (Node current: nodes) {
            for (Node n: current.neighbour) {
                if (current.compareTo(n) == -1) {
                    addDirectedEdge(current, n);
                } 
            }
        }
    /* 
        Queue<Node> queue = new LinkedList<>();
        int[] visited = new int[nodes.size()];     
        for (Node node: nodes) {
            if (visited[node.index] == 0) {
                queue.add(nodes.get(node.index));
                visited[node.index] = 1; 
                while (!queue.isEmpty()) {
                    Node current = queue.remove();
                    for (Node n: current.neighbour) {
                        if (visited[n.index] == 0) {
                            queue.add(n);
                            visited[n.index] = 1;
                        }           
                        if (current.compareTo(n) == -1) {
                            addDirectedEdge(current, n);
                        } 

                    }
                }
            }
        }
    */
    }

    public void printDAG() {
        for (Node n: nodes) {
            System.out.print(n.index + ":");
            for (Node neighbour: n.out_neighbour) {
                System.out.print(" " + neighbour.index);
            }
            System.out.println();
        }
    }

    public int[][][] DPTriangularPathCount(int k) {
        int[][][] H = new int[nodes.size()][nodes.size()][k];
        /*Queue<Node> queue = new LinkedList<>();
        int[] visited = new int[nodes.size()];
        for (Node node: nodes) {
            if (visited[node.index] == 0) {
                queue.add(nodes.get(node.index));
                visited[node.index] = 1; 
                while (!queue.isEmpty()) {
                    Node current = queue.remove();
                    for (Node n: current.out_neighbour) {
                        H[current.index][n.index][1] = 1;
                        if (visited[n.index] == 0) {
                            queue.add(n);
                            visited[n.index] = 1;
                        }           
                    }
                }
            }
        }*/
        for (Node current: nodes) {
            for (Node n: current.out_neighbour) {
                    H[current.index][n.index][1] = 1;
            }
        }

        for (int j = 2; j < k; j++) {
            for (int i = 0; i < nodes.size(); i++) {
                for (Node x: nodes.get(i).out_neighbour) {
                    for (Node t: nodes.get(x.index).out_neighbour) {
                        if (H[i][t.index][1] == 1) {
                            H[i][x.index][j] += H[x.index][t.index][j-1];
                        }
                    }
                }
            }         
        }
        return H;
    }

    public Node sampleWithProbability(double[] prob, List<Node> nodes) {
        double rand = Math.random();
        double F = 0;
        for (int i = 0; i < nodes.size(); i++) {
            F += prob[i];
            if (rand < F) {
                return nodes.get(i);
            }
        }
        return null;
    }

 
    public List<Node> DPTriangularPathSampling(int[][][] H, int k) {
        List<Node> path = new ArrayList<>();
        List<Node> Q = nodes;
        int last = -1;
        int secLast = -1;
        int[] c = new int[nodes.size()];
        for (int i = 0; i < c.length; i++) {
            Node u = Q.get(i);
            int count = 0;
            for (Node v: u.out_neighbour) {
                count += H[u.index][v.index][k-1];
            }
            c[u.index] = count;
        }
        /*System.out.println("#########");
        for (int p: c) {
            System.out.print(p + " ");
        }*/
        for (int i = 0; i < k; i++) {
            double[] prob = new double[Q.size()];
            if (i == 0) {
                int cnt = Arrays.stream(c).sum();
                for (int j = 0; j < prob.length; j++) {
                    prob[j] = ((double) c[j])/cnt;
                    //System.out.println(prob[j]);
                }
            } else if (i == 1) {
                for (int j = 0; j < prob.length; j++) {
                    prob[j] = ((double) H[last][Q.get(j).index][k-1])/c[last];
                    /*System.out.println("c[last]: " + c[last]);
                    System.out.println("fact: " + H[last][Q.get(j).index][k-2]);
                    System.out.println("save me");
                    System.out.println(prob[j]);*/
                }
            } else {
                for (int j = 0; j < prob.length; j++) {
                    prob[j] = ((double) H[last][Q.get(j).index][k-i])/H[secLast][last][k-i+1];
                    /*System.out.println(H[last][Q.get(j).index][k-i]);
                    System.out.println("secLast: " + secLast);
                    System.out.println("last: "+ last);
                    System.out.println(H[secLast][last][k-i+1]);
                    System.out.println(prob[j]);*/
                }
            }
            /*System.out.println(Q);
            System.out.println("###########prob##########");
            for (double l: prob) {
                System.out.println(l);
            }*/
            Node selected = sampleWithProbability(prob, Q);
            //System.out.println("what");
            //System.out.println(selected.index);
            path.add(selected);
            secLast = last;
            last = selected.index;
            Q = selected.out_neighbour;
        }

        return path;
    }



    
}