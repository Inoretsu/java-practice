import javafx.util.Pair;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Vector;

public class Algorithm {
    private Graph graph;
    private int n;
    private byte matrix[][];
    private Vector<Pair<Node, Node>> edges;
    private int it = 0;

    Algorithm(Graph gr) {
        graph = gr;
    }

    public void init(){
        edges = new Vector<Pair<Node, Node>>(){};
        n = graph.getNodeCount();
        matrix = new byte[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = graph.getNode(i).hasEdgeToward(j) ? (byte)1 : 0;
    }

    public void calculate(){
        for( Node first : graph ){ //for every node
            for( Edge firstConntections : first.getEachEdge() ){
                for( Edge secondConnections : firstConntections.getTargetNode().getEachEdge()) {
                    if ( !first.hasEdgeBetween(secondConnections.getTargetNode().getId()) && secondConnections.getTargetNode() != first){
                        edges.add(new Pair<>(first, secondConnections.getTargetNode()));
                    }
                }
            }
        }


                }

    public void step(){
        NodeChangeListener.getInstance().addEdge(edges.get(it).getKey(), edges.get(it).getValue());
        ++it;
    }

    public void create(){
        for( Pair n : edges )
            step();
    }



}
