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
        System.out.println("mouseReleased\n");
        if( e.getButton() == MouseEvent.BUTTON1 ){
            x2 = e.getX();
            y2 = e.getY();
            Collection<GraphicElement> selected = vw.allNodesOrSpritesIn(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
            for( GraphicElement ge : selected )
                graph.getNode(ge.getId()).setAttribute("ui.selected", true);
        }
        pipe.pump();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked\n");
        switch (e.getButton()){
            case MouseEvent.BUTTON1:
                //Select one
                Collection<GraphicElement> selected = vw.allNodesOrSpritesIn(e.getX()-5, e.getY()-5, e.getX()+5, e.getY()+5);
                if( !selected.isEmpty() ){
                    for( GraphicElement ge : selected ) {
                        graph.getNode(ge.getId()).setAttribute("ui.selected", true);
                        System.out.println(graph.getNode(ge.getId()));
                    }
                }
                else
                    //Deselect
                    if( !cm.isVisible() )
                        for( Node c : graph.getNodeSet() )
                            c.setAttribute("ui.selected", false);
               //Double click
                if(e.getClickCount() >= 2)
                    NodeSet.addNode(graph,"");
                break;

            case MouseEvent.BUTTON3:
                cm.show(e.getComponent(), e.getX(), e.getY());
                break;
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
        System.out.println("mousePressed\n");
        System.out.println(e.getX());
        System.out.println(' ');
        System.out.println(e.getY());
        x1 = e.getX();
        y1 = e.getY();
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
