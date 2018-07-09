import javax.swing.*;
import org.graphstream.graph.*;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.awt.*;
import java.util.Vector;

public class ContextMenu extends JPopupMenu {
    public ContextMenu(Graph graph, GraphicGraph grGraph){
        this.setBackground(new Color(34, 31, 37));
        this.setPreferredSize(new Dimension(130, 150));

        Font menufont = new Font("Exo 2 light", Font.CENTER_BASELINE, 12);

        JMenuItem createNode = new JMenuItem("Add node...");
        createNode.setBackground(new Color(34, 31, 37));
        createNode.setFont( menufont);
        createNode.setForeground(Color.white);
        add(createNode);
        createNode.addActionListener((e) -> {
            JFrame input = new JFrame("Name");
            input.setLayout(new GridLayout(3,1,50,3));
            Label lbl = new Label("   Enter the node name");
                lbl.setFont(menufont);
                lbl.setForeground(Color.white);
                lbl.setBackground(new Color(34, 31, 37));
            input.add(lbl);
            JTextField name = new JTextField();
            name.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            input.add(name);
            JButton ok = new JButton("ok");
                ok.setPreferredSize(new Dimension(27, 60));
                ok.setFocusPainted(false);
                ok.setBackground(new Color(66,62,70));
                ok.setFont(new Font("Exo 2 medium", Font.CENTER_BASELINE, 17));
                ok.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                ok.setForeground(Color.white);
            input.add(ok);
            ok.addActionListener((ee)->{
                NodeChangeListener.getInstance().addNode(name.getText());
            });
            input.setBounds(500, 200, 260,160);
            input.setVisible(true);
        });

        JMenuItem deleteNodes = new JMenuItem("Delete node(-s)");
        deleteNodes.setBackground(new Color(34, 31, 37));
        deleteNodes.setFont( menufont);
        deleteNodes.setForeground(Color.white);
        add(deleteNodes);
        deleteNodes.addActionListener((e) -> {
            Vector<Node> b = new Vector<>(grGraph.getNodeSet());
            for( Node n : b ) //Getting all selected nodes
                if (n.hasAttribute("ui.selected"))
                   NodeChangeListener.getInstance().deleteNode(graph.getNode(n.getId())); //Hew-hew-hew
        });

        JMenuItem boundAll = new JMenuItem("Bound all");
        boundAll.setBackground(new Color(34, 31, 37));
        boundAll.setFont( menufont);
        boundAll.setForeground(Color.white);
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
        boundWith.setBackground(new Color(34, 31, 37));
        boundWith.setFont( menufont);
        boundWith.setForeground(Color.white);
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
        deleteEdges.setBackground(new Color(34, 31, 37));
        deleteEdges.setFont( menufont);
        deleteEdges.setForeground(Color.white);
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
