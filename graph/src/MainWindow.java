import java.awt.*;
import javax.swing.*;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSourceDGS;
import org.graphstream.ui.view.*;
import org.graphstream.algorithm.generator.*;
import org.graphstream.ui.swingViewer.*;
import org.graphstream.graph.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.ui.view.util.MouseManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.stream.file.FileSourceFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MainWindow extends JFrame {
    private Graph graph;

    private Viewer viewer;
    private ViewPanel view;

    private JTable table;

    CustomMouseManager mouseMan;

    private final JLabel title1 = createLabel("Graph creator", 22, 22);
    private final JLabel title2 = createLabel("Algorithm", 22, 240);
    private final JLabel title3 = createLabel("Matrix", 22, 433);

    private final JTextField nodeName = createTextEdit ("Enter node name",22, 63, 211, 37);
    private final JButton addButton = createButton("add node", 22, 114);
    private final JButton generateButton = createButton("random generator", 22, 164);

    private final JCheckBox sbs = createCheckbox ("step by step", 22, 278);
    private final JButton startButton = createButton ("start", 22, 307);
    private final JButton clearButton = createButton ("clear", 22, 358);
    private final JMenuItem openItem = new JMenuItem("Open");
    private final JMenuItem saveItem = new JMenuItem("Save");

    public MainWindow() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("ID");
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        view = viewer.addDefaultView(false);

        mouseMan = new CustomMouseManager();
        mouseMan.init(viewer.getGraphicGraph(), view);
        mouseMan.enableContext(graph);

        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet",
                "node {" +
                        "shape: circle;" +
                        "fill-color: white;" +
                        "size: 40px, 40px;" +
                        "stroke-mode: plain;" +
                        "stroke-color: rgb(220,45,45);" +
                        "stroke-width: 5px;" +
                        "text-color: rgb(220,45,45);" +
                        "text-style: bold;" +
                        "text-size: 20px;" +
                        "text-mode: normal;" +
                        //"text-mode: hidden;" +
                        "}" +
                        "node:selected {" +
                        "stroke-color: rgb(34,31,37);" +
                        "text-color: rgb(34,31,37);" +
                        "}" +
                        "node:clicked {" +
                        "fill-color: rgb(220,45,45);" +
                        "text-color: white;" +
                        "}" +
                        " edge {" +
                        "shape: line;" +
                        "size: 3px;" +
                        "arrow-size: 10px;" +
                        "arrow-shape: arrow;" +
                        "fill-color: rgb(34,31,37);" +
                        "}");


        //file menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Exo 2 light", Font.CENTER_BASELINE, 11));
        fileMenu.setForeground(new Color(232,224,239));
        menuBar.setBackground(new Color(66,62,70));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(179,45,45)));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(e -> System.exit(0));

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setFont(new Font("Exo 2 light", Font.CENTER_BASELINE, 11));
        aboutMenu.setForeground(new Color(232,224,239));

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        //left panel
        JPanel toolBar = new JPanel();

        toolBar.setPreferredSize(new Dimension(260, 660));
        toolBar.setBackground(new Color(34,31,37));
        toolBar.setLayout(null);
        toolBar.add(title1);
        toolBar.add(nodeName);
        toolBar.add(addButton);
        toolBar.add(generateButton);
        toolBar.add(title2);
        toolBar.add(sbs);
        toolBar.add(startButton);
        toolBar.add(clearButton);
        toolBar.add(title3);
        JScrollPane jscrlp = new JScrollPane(table);
        jscrlp.setBounds(22, 464, 211, 185);
        toolBar.add(jscrlp);
        //

        view.setPreferredSize(new Dimension(950, 660));

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setPreferredSize(new Dimension(1200, 660));
        backPanel.add(toolBar, BorderLayout.EAST);
        backPanel.add(view, BorderLayout.WEST);

        setContentPane(backPanel);
        viewer.enableAutoLayout();

        addButton.addActionListener((e) -> {
            Node n = graph.addNode( Integer.toString(IDGenerator.getInstance().assignID()) );
            n.addAttribute("ui.label", nodeName.getText());
        });

        generateButton.addActionListener((e) -> {
            Generator gen = new RandomGenerator();
            gen.addSink(graph);
            gen.begin();
            Node m = graph.getNode(graph.getNodeCount() - 1);
            m.setAttribute("ui.label", graph.getNodeCount());
            int n = ThreadLocalRandom.current().nextInt(3, 25);
            for(int i=0; i<n; i++) {

                gen.nextEvents();
                Node curr = graph.getNode(graph.getNodeCount() - 1);
                curr.setAttribute("ui.label", graph.getNodeCount());
            }
            Node g = graph.getNode(graph.getNodeCount() - 1);
            g.setAttribute("ui.label", graph.getNodeCount());
            gen.end();
        });

        clearButton.addActionListener((e) -> {
            Node n;
            while( graph.getNodeCount() != 0 )
                graph.removeNode(0);
        });

        // save/open file
        saveItem.addActionListener(e -> {
            JFileChooser saveFile = new JFileChooser();
            saveFile.showSaveDialog(null);
            String path = saveFile.getSelectedFile().toString();

            File file = new File(path);
            try {
                graph.write(String.valueOf(file));

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        openItem.addActionListener(e -> {
            JFileChooser openFile = new JFileChooser();
            openFile.showOpenDialog(null);
            File path = openFile.getSelectedFile();

            FileSource files = new FileSourceDGS();
            files.addSink(graph);
            try {
                files.begin(String.valueOf(path));
                while(files.nextEvents()) {}
                files.end();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        //-------------------------------------

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JLabel createLabel (String text, int x, int y)
    {
        JLabel te = new JLabel(text);
        te.setBounds(x, y, 211, 28);
        te.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Font font = new Font("Exo 2 medium", Font.CENTER_BASELINE, 24);
        te.setFont(font);
        te.setForeground(Color.white);
        return te;
    }

    private JTextField createTextEdit (String text, int x, int y, int w, int h)
    {
        JTextField ta = new JTextField();
        ta.setBounds(x, y, w, h);
        ta.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        ta.setFont(new Font("Exo 2 light", Font.CENTER_BASELINE, 14));
        ta.setBackground(new Color(199,199,199));
        return ta;
    }

    private JCheckBox createCheckbox (String text, int x, int y)
    {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setBounds(x, y, 211, 16);
        checkBox.setFocusPainted(false);
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Font font = new Font("Exo 2 thin", Font.CENTER_BASELINE, 13);
        checkBox.setFont(font);
        checkBox.setForeground(Color.white);
        checkBox.setBackground(new Color(34, 31, 37));
        return checkBox;
    }

    private JButton createButton (String text, int x, int y)
    {
        JButton button = new JButton(text);
        button.setBounds(x, y, 211, 37);
        button.setFocusPainted(false);
        button.setBackground(new Color(66,62,70));
        button.setFont(new Font("Exo 2 medium", Font.CENTER_BASELINE, 17));
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        button.setForeground(Color.white);
        return button;
    }
}