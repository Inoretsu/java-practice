import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

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

        ((DefaultTableModel)table.getModel()).addColumn("");
        table.getColumnModel().getColumn(0).setIdentifier("header");
    }

    public void addNode(String name){
        if( name.isEmpty() ) name = Integer.toString(ID);
        Node n = graph.addNode(Integer.toString(ID++));
        n.addAttribute("ui.label", name);

        TableColumn c = new TableColumn();
        c.setHeaderValue(name);
        c.setIdentifier(n);
        table.addColumn(c);

        ((DefaultTableModel)table.getModel()).addRow(new Object[]{});
        table.getModel().setValueAt(name, table.getRowCount() - 1, 0);
    }

    public void deleteNode(Node n) {
        ((DefaultTableModel)table.getModel()).removeRow(table.getColumn(n).getModelIndex());
        table.removeColumn(table.getColumn(n));
        graph.removeNode(n);
    }

    public void updateTable(){
        for( Node n : graph ) {
            TableColumn c = new TableColumn();
            c.setHeaderValue(n.getAttribute("ui.label"));
            c.setIdentifier(n);
            table.addColumn(c);
        }
        for( Node n : graph) {
            ((DefaultTableModel) table.getModel()).addRow(new String[table.getColumnCount()]);
        }
    }

    public void clean(){
        ID = 0;
        while( graph.getNodeCount() != 0 )
            deleteNode(graph.getNode(0));
    }

    public Vector<String> listConnections(Node n){
        Vector<String> data = new Vector<>();
        data.add(n.getAttribute("ui.label"));
        for( TableColumn c : Collections.list(table.getColumnModel().getColumns()) ){
            if( c.getModelIndex() != 0 )
                if( n.hasEdgeBetween( (Node)c.getIdentifier() ) )
                    data.add("Y");
                else
                    data.add("");
        }
        return data;
    }
}


