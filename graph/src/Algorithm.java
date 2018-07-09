import javafx.util.Pair;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Vector;

public class Algorithm {
    private Graph graph;
    private Graph buff;
    private int n;
    private byte matrix[][];
    private Vector< Pair< Node, Pair<Node, Node> > > steps; //Source, first connection, second - single path
    private int stepNum = 0;
    private int nodeToCol = 0;

    Algorithm(Graph gr) {
        graph = gr;
    }

    public void init(){
        steps = new Vector< Pair< Node, Pair<Node, Node> > >(){};
        n = graph.getNodeCount();
        matrix = new byte[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = graph.getNode(i).hasEdgeToward(j) ? (byte)1 : 0;
    }

    /*public void calculate(){
        for( Node first : graph ){ //for every node
            for( Edge firstConntection : first.getEachEdge() ){
                for( Edge secondConnection : firstConntection.getTargetNode().getEachEdge()) {
                    if ( !first.hasEdgeBetween(secondConnection.getTargetNode().getId()) && secondConnection.getTargetNode() != first){
                        steps.add(  new Pair( first, new Pair( firstConntection.getTargetNode(), secondConnection.getTargetNode() ) )   );
                    }
                }
            }
        }
    }*/

    public void calculate(){
        for (int k = 0; k < n; k++) { //First node index
            for (int i = 0; i < n; i++) { //Connections of the first
                if( matrix[k][i] != 0 && i != k ) {
                    for (int j = 0; j < n; j++) { //Connection of the connections
                        if( matrix[i][j] == 1 && j != i && matrix[k][j] == 0 && j != k  ){
                            matrix[k][j] = 1;
                            steps.add(  new Pair( graph.getNode(k), new Pair( graph.getNode(i), graph.getNode(j)) )   );
                        }
                    }
                }
            }
        }
    }

    public void step(){
        if( stepNum >= steps.size() ) return;
        Node f = steps.get(stepNum).getKey();
        Node s = steps.get(stepNum).getValue().getKey();
        Node th = steps.get(stepNum).getValue().getValue();

        switch (nodeToCol){
            case 0:
                f.addAttribute("ui.class", "nodemark");
                ++nodeToCol;
                break;
            case 1:
                s.addAttribute("ui.class", "nodemark");
                f.getEdgeBetween(s).addAttribute("ui.class", "edgemark");
                ++nodeToCol;
                break;
            case 2:
                th.addAttribute("ui.class", "nodemark");
                s.getEdgeBetween(th).addAttribute("ui.class", "edgemark");
                ++nodeToCol;
                break;
                default:
                    f.getEdgeBetween(s).removeAttribute("ui.class");
                    s.getEdgeBetween(th).removeAttribute("ui.class");

                    NodeChangeListener.getInstance().addEdge(f, th);
                    nodeToCol = 0;
                    ++stepNum;
                    step();
                    break;
        }
    }

    public void create(){
        while (stepNum < steps.size())
            step();
    }
}
