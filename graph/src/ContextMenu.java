import javax.swing.*;
import org.graphstream.graph.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.awt.*;
import java.util.Vector;

public class ContextMenu extends JPopupMenu {
    public ContextMenu(Graph graph, GraphicGraph grGraph){

        JMenuItem createNode = new JMenuItem("Add node...");
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

        JMenuItem deleteNodes = new JMenuItem("Delete node(-s)");
        add(deleteNodes);
        deleteNodes.addActionListener((e) -> {
            Vector<Node> b = new Vector<>(grGraph.getNodeSet());
            for( Node n : b ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                   NodeChangeListener.getInstance().deleteNode(graph.getNode(n.getId())); //Hew-hew-hew
        });

        JMenuItem boundAll = new JMenuItem("Bound all");
        add(boundAll);
        boundAll.addActionListener((e) -> {
            Vector<Node> attrib = new Vector<>();
            for( Node n : grGraph.getNodeSet() ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                    attrib.add(graph.getNode(n.getId()));

                for( Node source : attrib )
                    for( Node dest : attrib )
                        if( !source.hasEdgeToward(dest) && dest != source )
                            NodeChangeListener.getInstance().addEdge(source, dest);
        });

        JMenuItem boundWith = new JMenuItem("Bound with...");
        add(boundWith);
        boundWith.addActionListener((e) -> {
            Node selected = null;
            Vector<Node> attr = new Vector<>();
            for( Node n : grGraph ) {
                if (n.hasAttribute("ui.selected") && !n.hasAttribute("ui.clicked"))
                    attr.add(n);
                else
                    if (n.hasAttribute("ui.clicked"))
                        selected = graph.getNode(n.getId());
            }

            for( Node nan : attr )
                NodeChangeListener.getInstance().addEdge(graph.getNode(nan.getId()), selected);
        });

        JMenuItem deleteEdges = new JMenuItem("Delete edge(-s)");
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
