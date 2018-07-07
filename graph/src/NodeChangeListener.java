import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class NodeChangeListener {
    private Graph graph;
    private JTable table;
    private int ID;

    private static NodeChangeListener instance = new NodeChangeListener();

    public NodeChangeListener(){}

    public static NodeChangeListener getInstance(){ return instance; }

    public void init(Graph gr, JTable tabel){
        table = tabel;
        graph = gr;
    }

    public void addNode(String name){
        Node n = graph.addNode(Integer.toString(ID++));
        n.addAttribute("ui.label",name);
        TableColumn c = new TableColumn();
        c.setHeaderValue(123);
        table.addColumn(new TableColumn(0));
    }
}
