import javafx.scene.control.TableColumn;
import org.graphstream.graph.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class GraphChangeListener {

    private Graph graph;
    private JTable table;
    byte adjacencyMatrix[][];
    HeaderParams current;

    public GraphChangeListener(Graph graph, JTable table){
        this.table = table;
        this.graph = graph;
    }

    public enum HeaderParams {
        LABEL, ID
    };

    public void createMatrix(){
        int n = graph.getNodeCount();
        adjacencyMatrix = new byte[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                adjacencyMatrix[i][j] = graph.getNode(i).hasEdgeBetween(j) ? (byte)1 : 0;
    }

    public byte[][] getAdjacencyMatrix(){
        return adjacencyMatrix;
    }

    public String[] getTableHeader(HeaderParams param){
        String header[] = new String[graph.getNodeCount()];
        int i = 0;
        switch (param){
            case ID:
                for( Node n : graph.getNodeSet() )
                    header[i++] = n.getId();
                current = HeaderParams.ID;
                break;

            case LABEL:
                for( Node n : graph.getNodeSet() )
                    header[i++] = n.getAttribute("ui.label");
                current = HeaderParams.LABEL;
                break;
        }
        return header;
    }

    public void deleteNode(Node n){
        int size = table.getColumnCount();
        int i = 0;
            table.removeColumn(table.getColumn(i));
        switch (current)
        {
            case LABEL:
                for(; i < size; ++i)
                {
                    if( table.getColumnName(i) == n.getAttribute("ui.label") ) {
                        table.removeColumn(table.getColumn(i));
                        ((DefaultTableModel)table.getModel()).removeRow(i);
                        return;
                    }
                }
                break;

            case ID:
                for(; i < size; ++i)
                {
                    if( table.getColumnName(i) == n.getId() ) {
                        table.removeColumn(table.getColumn(i));
                        ((DefaultTableModel) table.getModel()).removeRow(i);
                        return;
                    }
                }
                break;
        }
    }

    public void addNode(Node n)
    {
        int size = graph.getNodeCount();
        TableColumn col = new TableColumn();
    }
}
