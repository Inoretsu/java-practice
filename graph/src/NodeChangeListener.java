import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class NodeChangeListener {
    private Graph graph;
    private JTable table;
    private DefaultTableModel model;
    private Vector<Node> ident;
    private int ID = 1;

    private static NodeChangeListener instance = new NodeChangeListener();

    public NodeChangeListener(){}

    public static NodeChangeListener getInstance(){ return instance; }

    public void init(Graph gr, JTable tabel){
        table = tabel;
        graph = gr;
        model = (DefaultTableModel)tabel.getModel();
        ident = new Vector<>();

        model.addColumn("");
        ident.add(null);
        model.setColumnIdentifiers(ident);
    }

    public void addNode(String name){
        if( name.isEmpty() ) name = Integer.toString(ID+1);
        Node n = graph.addNode(Integer.toString(ID++));
        n.addAttribute("ui.label", name);

        model.addColumn(name);
        ident.setElementAt(n, ident.size()-1);
        for( int i = 1; i < table.getColumnCount(); ++i ) //Duct tape
            table.getColumnModel().getColumn(i).setIdentifier(ident.get(i));

        model.addRow(new Object[]{name});
        System.out.println(table.getColumn(n).getModelIndex());

    }

    public void deleteNode(Node n) {
        //System.out.println(table.getColumn(n).getModelIndex());
        model.removeRow(0);
        table.removeColumn(table.getColumn(n));
        graph.removeNode(n);
    }

    public void updateTable(){
        for( Node n : graph ) {
           model.addColumn(ID++);
           ident.setElementAt(n, ident.size()-1);
           for( int i = 1; i < table.getColumnCount(); ++i ) //Duct tape
               table.getColumnModel().getColumn(i).setIdentifier(ident.get(i));
        }
        for( Node n : graph)
            model.addRow(listConnections(n));
    }

    public void clean(){
        while( graph.getNodeCount() != 0 )
            deleteNode(graph.getNode(0));
        ID = 1;
        ident.clear();
        ident.add(null);
    }

    public Vector<String> listConnections(Node n){
        Vector<String> data = new Vector<>();
        data.add(n.getAttribute("ui.label"));
        for( Node nan : ident )
        {
            if( nan != null )
                if ( n.hasEdgeBetween(nan) )
                    data.add("Y");
                else
                    data.add("");
        }
        return data;
    }

    public void addEdge(Node s, Node d){
        if( graph.getEdge(s.getId()+ "_" + d.getId()) == null )
            graph.addEdge(s.getId() + "_" + d.getId(),s,d,true);
        else
            table.setValueAt("Y", table.getColumn(s).getModelIndex(), table.getColumn(d).getModelIndex());
    }

    public void deleteEdge(Node s, Node d){
        table.setValueAt("", s.getIndex(), d.getIndex());
        graph.removeEdge(s,d);

    }
}


