import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.*;

import org.graphstream.ui.graphicGraph.GraphicElement;

import java.util.*;

public class MouseListenerCustom implements MouseInputListener{
    private View vw;
    private Graph graph;
    private ContextMenu cm;
    private ViewerPipe pipe;

    private double x1,y1;
    private double x2,y2;

    public MouseListenerCustom(View vw, Graph g, ViewerPipe viewerPipe) {
        this.pipe = viewerPipe;
        this.vw = vw;
        this.graph = g;
        this.vw.addMouseListener(this);
        this.cm = new ContextMenu(g);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if( e.getButton() == MouseEvent.BUTTON1 ){
            x2 = e.getX();
            y2 = e.getY();
            Collection<GraphicElement> selected = vw.allNodesOrSpritesIn(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
            for( GraphicElement ge : selected )
            {
                graph.getNode(ge.getId()).setAttribute("Selected", true);
                graph.getNode(ge.getId()).setAttribute("ui.label", "Selected");
            }
        }
        pipe.pump();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()){
            case MouseEvent.BUTTON1:
                //Select one
                Collection<GraphicElement> selected = vw.allNodesOrSpritesIn(e.getX()-5, e.getY()-5, e.getX()+5, e.getY()+5);
                if( !selected.isEmpty() ){
                    for( GraphicElement ge : selected )
                    {
                        graph.getNode(ge.getId()).setAttribute("Selected", true);
                        graph.getNode(ge.getId()).setAttribute("ui.label", "Selected");
                    }
                }
                else
                    //Deselect
                    if( !cm.isVisible() )
                        for( Node c : graph.getNodeSet() ) {
                            c.setAttribute("ui.label", "");
                            c.setAttribute("Selected", false);
                        }
                //Double click
                if(e.getClickCount() >= 2) {
                    Node c = graph.addNode(Integer.toString(graph.getNodeCount()));
                    c.addAttribute("Selected", false);
                    c.setAttribute("ui.label", "");
                }
                break;

            case MouseEvent.BUTTON3:
                cm.show(e.getComponent(), e.getX(), e.getY());
        }
        pipe.pump();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        //System.out.println("Pump it!");
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
}
