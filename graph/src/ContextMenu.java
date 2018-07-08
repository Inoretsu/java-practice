import javax.swing.*;
import org.graphstream.graph.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.awt.*;
import java.util.Vector;

public class ContextMenu extends JPopupMenu {
    private JMenuItem boundAll;
    private JMenuItem deleteEdges;
    private JMenuItem createNode;
    private JMenuItem deleteNodes;

    private Graph graph;
    private GraphicGraph grGraph;


    public ContextMenu(Graph graph, GraphicGraph grGraph){
        this.graph = graph;
        this.grGraph = grGraph;

        createNode = new JMenuItem("Add node...");
        add(createNode);
        createNode.addActionListener((e) -> {
            JFrame input = new JFrame("Name");
            input.setLayout(new GridLayout(3,1,50,3));
            input.add(new Label("Enter the node name"));
            JTextField name = new JTextField();
            input.add(name);
            JButton ok = new JButton("Ok");
            input.add(ok);
            ok.addActionListener((ee)->{
                NodeChangeListener.getInstance().addNode(name.getText());
            });
            input.setMinimumSize(new Dimension(200,150));
            input.setVisible(true);
        });

        deleteNodes = new JMenuItem("Delete node(-s)");
        add(deleteNodes);
        deleteNodes.addActionListener((e) -> {
            Vector<Node> b = new Vector<>(grGraph.getNodeSet());
            for( Node n : b ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                   NodeChangeListener.getInstance().deleteNode(graph.getNode(n.getId())); //Hew-hew-hew
        });

        boundAll = new JMenuItem("Bound");
        add(boundAll);
        boundAll.addActionListener((e) -> {
            Vector<Node> attrib = new Vector<>();
            for( Node n : graph.getNodeSet() ) //Getting all selected nodes
            {
                if (n.hasAttribute("ui.selected")) {
                    attrib.add(n);
                }
            }
        });

        deleteEdges = new JMenuItem("Delete edge(-s)");
        add(deleteEdges);
        deleteEdges.addActionListener((e) -> {
            Vector<Node> attrib = new Vector<>();
            for( Node n : grGraph.getNodeSet() ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                    attrib.add(graph.getNode(n.getId()));

            for( Node source : attrib )
                for( Node dest : attrib )
                    if( source.hasEdgeToward(dest) )
                        NodeChangeListener.getInstance().deleteEdge(source, dest);
        });




    }
}
