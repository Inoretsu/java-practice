import org.graphstream.graph.Graph;
import org.graphstream.ui.view.util.*;

import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicSprite;
import org.graphstream.ui.view.View;

import java.awt.event.MouseEvent;

public class CustomMouseManager implements MouseManager
{
    protected View view;
    protected GraphicGraph graph;
    protected Graph defaultGraph;
    protected ContextMenu contMenu;

    public void init(GraphicGraph graph, View view) {
        this.view = view;
        this.graph = graph;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    public void enableContext(Graph graph){
        defaultGraph = graph;
        this.contMenu = new ContextMenu(graph);
    }

    public void release() {
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
    }

    protected void mouseButtonPress(MouseEvent event) {
        view.requestFocus();

        // Unselect all.

        if (!event.isShiftDown()) {
            for (Node node : graph) {
                if (node.hasAttribute("ui.selected"))
                    node.removeAttribute("ui.selected");
            }

            for (GraphicSprite sprite : graph.spriteSet()) {
                if (sprite.hasAttribute("ui.selected"))
                    sprite.removeAttribute("ui.selected");
            }
        }
    }

    protected void mouseButtonRelease(MouseEvent event,
                                      Iterable<GraphicElement> elementsInArea) {
        for (GraphicElement element : elementsInArea) {
            if (!element.hasAttribute("ui.selected"))
                element.addAttribute("ui.selected");
        }
    }

    protected void mouseButtonPressOnElement(GraphicElement element,
                                             MouseEvent event) {
        view.freezeElement(element, true);
        if (event.getButton() == 3) {
            element.addAttribute("ui.selected");
        } else {
            element.addAttribute("ui.clicked");
        }
    }

    protected void elementMoving(GraphicElement element, MouseEvent event) {
        view.moveElementAtPx(element, event.getX(), event.getY());
    }

    protected void mouseButtonReleaseOffElement(GraphicElement element,
                                                MouseEvent event) {
        view.freezeElement(element, false);
        if (event.getButton() != 3) {
            element.removeAttribute("ui.clicked");
        } else {
        }
    }

    // Mouse Listener

    protected GraphicElement curElement;

    protected float x1, y1;

    public void mouseClicked(MouseEvent event) {
        switch (event.getButton()){
            case MouseEvent.BUTTON1:
                if(event.getClickCount() > 1)
                    NodeChangeListener.getInstance().addNode("");
                break;
            case MouseEvent.BUTTON3:
                if( contMenu != null )
                    contMenu.show(event.getComponent(), event.getX(), event.getY());
                break;
        }
    }

    public void mousePressed(MouseEvent event) {
        curElement = view.findNodeOrSpriteAt(event.getX(), event.getY());

        if (curElement != null) {
            mouseButtonPressOnElement(curElement, event);
        } else {
            x1 = event.getX();
            y1 = event.getY();
            mouseButtonPress(event);
            view.beginSelectionAt(x1, y1);
        }
    }

    public void mouseDragged(MouseEvent event) {
        if (curElement != null) {
            elementMoving(curElement, event);
        } else {
            view.selectionGrowsAt(event.getX(), event.getY());
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (curElement != null) {
            mouseButtonReleaseOffElement(curElement, event);
            curElement = null;
        } else {
            float x2 = event.getX();
            float y2 = event.getY();
            float t;

            if (x1 > x2) {
                t = x1;
                x1 = x2;
                x2 = t;
            }
            if (y1 > y2) {
                t = y1;
                y1 = y2;
                y2 = t;
            }

            mouseButtonRelease(event, view.allNodesOrSpritesIn(x1, y1, x2, y2));
            view.endSelectionAt(x2, y2);
        }
    }

    public void mouseEntered(MouseEvent event) {
        // NOP
    }

    public void mouseExited(MouseEvent event) {
        // NOP
    }

    public void mouseMoved(MouseEvent e) {
    }
}