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

        deleteNodes = new JMenuItem("Delete node(-s)");
        add(deleteNodes);
        deleteNodes.addActionListener((e) -> {
            for( Node n : graph.getNodeSet() ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                    graph.removeNode(n);
        });

        deleteEdges = new JMenuItem("Delete edge(-s)");
        add(deleteEdges);
        deleteEdges.addActionListener((e) -> {
            Vector<Node> attrib = new Vector<>();
            for( Node n : graph.getNodeSet() ) //Getting all selected nodes
                if ((boolean)n.getAttribute("Selected"))
                    attrib.add(n);

            for( Node n : attrib )
                for( Node c : attrib )
                    if( n.hasEdgeBetween(c) )
                        graph.removeEdge(n,c);
        });

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


    }
}
