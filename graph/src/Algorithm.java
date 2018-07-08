import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Vector;

public class Algorithm {
    private Graph graph;
    private int n;
    private byte matrix[][];
    private Vector<Node> edges;
    private int it = 0;
    private Node source;

    Algorithm(Graph gr) {
        graph = gr;
    }

    public void init(Node s){
        //edges.clear();
        //it = 0;

        this.source = s;
        edges = new Vector<Node>(){};
        n = graph.getNodeCount();
        matrix = new byte[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = graph.getNode(i).hasEdgeToward(j) ? (byte)1 : 0;
    }

    public void calculate(){
        for (int i = 0; i < n; i++) { //source connections
            if (matrix[source.getIndex()][i] == 1) { //if there is one
                for (int j = 0; j < n; j++) { //connections for connection
                    if (matrix[i][j] == 1 && matrix[source.getIndex()][j] == 0) { //if connections connection exists and source connection does not
                        matrix[i][j] = 1;
                        edges.add(graph.getNode(j));
                    }
                }
            }
        }
    }

    public void step(){
        NodeChangeListener.getInstance().addEdge(source, edges.get(it++));
    }

    public void create(){
        for( Node n : edges )
            NodeChangeListener.getInstance().addEdge(source, edges.get(it++));
    }



}
