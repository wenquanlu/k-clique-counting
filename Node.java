import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node> {
    int color=-1;
    int index;
    int out_degree=0;
    int in_degree=0;
    List<Node> in_neighbour = new ArrayList<>();
    List<Node> out_neighbour = new ArrayList<>();
    List<Node> neighbour = new ArrayList<>();

    public boolean add_in_neighbour(Node u) {
        if (in_neighbour.add(u)) {
            in_degree += 1;
            return true;
        }
        return false;
    }

    public boolean add_out_neighbour(Node u) {
        if (out_neighbour.add(u)) {
            out_degree += 1;
            return true;
        }
        return false;
    }

    public boolean add_neighbour(Node u) {
        if (neighbour.add(u)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Node o) {
        if (this.color > o.color) {
            return 1;
        } else if (this.color < o.color) {
            return -1;
        } else {
            if (this.index > o.index) {
                return 1;
            } else {
                return -1;
            }
        }
    }


}
