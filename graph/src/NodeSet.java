import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class NodeSet {
    public static void addNode(Graph g, String name){
        Node c = g.addNode(Integer.toString(IDGenerator.getInstance().assignID()));
        c.addAttribute("ui.label", name);
        c.addAttribute("ui.selected", false);
    }
}
