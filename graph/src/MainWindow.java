import java.awt.*;
import javax.swing.*;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.*;

import org.graphstream.stream.file.FileSourceDGS;

import org.graphstream.ui.swingViewer.*;
import org.graphstream.ui.view.*;

import org.graphstream.algorithm.generator.*;

import org.graphstream.stream.file.FileSource;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MainWindow extends JFrame {
    private Graph graph;

    private Viewer viewer;
    private ViewPanel view;

    private JTable table;

    CustomMouseManager mouseMan;
    private Algorithm algos;

    private final JLabel title1 = createLabel("Graph creator", 22, 22);
    private final JLabel title2 = createLabel("Algorithm", 22, 240);
    private final JLabel title3 = createLabel("Matrix", 22, 427);

    private final JTextField nodeName = createTextEdit ("Enter node name",22, 63, 211, 37);
    private final JButton addButton = createButton("add node", 22, 114);
    private final JButton generateButton = createButton("random generator", 22, 164);

    private final JCheckBox sbs = createCheckbox ("step by step", 22, 278);
    private final JButton startButton = createButton ("start", 22, 307);
    private final JButton clearButton = createButton ("clear", 22, 358);
    private final JMenuItem openItem = new JMenuItem("Open");
    private final JMenuItem saveItem = new JMenuItem("Save");

    public MainWindow() throws IOException {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("ID");
        graph.setStrict(false);
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        view = viewer.addDefaultView(false);
        viewer.enableAutoLayout();



        mouseMan = new CustomMouseManager();
        mouseMan.init(viewer.getGraphicGraph(), view);
        mouseMan.enableContext(graph);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/Exo2-Medium.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/Exo2-Light.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/Exo2-Thin.ttf")));
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

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
                        "shadow-color: rgb(220,45,45), white;" +
                        "shadow-mode: gradient-radial;" +
                        "shadow-offset: 0;" +
                        "shadow-width: 7px;" +
                        "}" +
                        "node:selected {" +
                        "stroke-color: rgb(34,31,37);" +
                        "text-color: rgb(34,31,37);" +
                        "}" +
                        " edge {" +
                        "shape: cubic-curve;" +
                        "size: 3px;" +
                        "arrow-size: 30px, 7px;" +
                        "arrow-shape: diamond;" +
                        "fill-color: rgb(34,31,37);" +
                        "}" +
                        "node.nodemark {" +
                        "shadow-width: 14px;" +
                        "}" +
                        "edge.edgemark {" +
                        "fill-color: rgb(220, 45, 45);" +
                        "}");


        //file menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        Font menufont = new Font("Exo 2 light", Font.CENTER_BASELINE, 12);
        fileMenu.setFont(menufont);
        fileMenu.setForeground(new Color(232,224,239));
        menuBar.setBackground(new Color(66,62,70));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(179,45,45)));
        openItem.setBackground(new Color(34, 31, 37));
        openItem.setPreferredSize( new Dimension(15, 25));
        saveItem.setBackground(new Color(34, 31, 37));
        saveItem.setPreferredSize( new Dimension(40, 25));
        openItem.setForeground(Color.white);
        openItem.setPreferredSize( new Dimension(40, 25));
        saveItem.setForeground(Color.white);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setBackground(new Color(34, 31, 37));
        exitItem.setPreferredSize( new Dimension(40, 25));
        exitItem.setForeground(Color.white);
        fileMenu.add(exitItem);
        exitItem.addActionListener(e -> System.exit(0));
        openItem.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        saveItem.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        exitItem.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        openItem.setFont(menufont);
        saveItem.setFont(menufont);
        exitItem.setFont(menufont);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        //left panel
        final Image backgroundImage = javax.imageio.ImageIO.read(new File("./res/background.png"));
        JPanel toolBar = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(backgroundImage, 0, 0, null);
            }
        };
        toolBar.setPreferredSize(new Dimension(260, 660));

       // toolBar.setBackground(new Color(34,31,37));
        toolBar.setLayout(null);
        toolBar.add(title1);
        toolBar.add(nodeName);
        toolBar.add(addButton);
        toolBar.add(generateButton);
        toolBar.add(title2);
        toolBar.add(sbs);
        startButton.setBackground(new Color(191, 40, 40));
        toolBar.add(startButton);
        toolBar.add(clearButton);
        toolBar.add(title3);

        // Table
        table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        NodeChangeListener.getInstance().init(graph, table);
        JScrollPane jscrlp = new JScrollPane(table);
        jscrlp.setBounds(22, 464, 211, 185);
        toolBar.add(jscrlp);

        view.setPreferredSize(new Dimension(950, 660));

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setPreferredSize(new Dimension(1200, 660));
        backPanel.add(toolBar, BorderLayout.EAST);
        backPanel.add(view, BorderLayout.WEST);
        setContentPane(backPanel);

        // Buttons
        addButton.addActionListener((e) -> {
            NodeChangeListener.getInstance().addNode(nodeName.getText());
        });

        generateButton.addActionListener((e) -> {
            NodeChangeListener.getInstance().clean();

            Generator gen = new RandomGenerator(3, false, true);
            gen.addSink(graph);
            gen.begin();

            int n = ThreadLocalRandom.current().nextInt(3, 10);
            for(int i=0; i<n; i++) {
                gen.nextEvents();
            }

            int i = 0;
            for( Node nen : graph )
                nen.setAttribute("ui.label", i++); //definitely not a duct tape

            NodeChangeListener.getInstance().updateTable();
            algos = null;
        });

        clearButton.addActionListener((e) -> {
            algos = null;
            NodeChangeListener.getInstance().clean();
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
            NodeChangeListener.getInstance().clean();

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

            NodeChangeListener.getInstance().updateTable();
        });

        startButton.addActionListener(e -> {
            if( algos == null ) {
                algos = new Algorithm(graph);
                algos.init();
                algos.calculate();
            }

            if( sbs.isSelected() )
                algos.step();
            else
                algos.create();
        });
        //-------------------------------------

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JLabel createLabel (String text, int x, int y)
    {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 211, 28);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Font font = new Font("Exo 2 medium", Font.CENTER_BASELINE, 24);
        label.setFont(font);
        label.setForeground(Color.white);
        return label;
    }

    private JTextField createTextEdit (String text, int x, int y, int w, int h)
    {
        JTextField textedit = new JTextField();
        textedit.setBounds(x, y, w, h);
        textedit.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        textedit.setFont(new Font("Exo 2 light", Font.CENTER_BASELINE, 14));
        textedit.setBackground(new Color(219, 219, 219));
        return textedit;
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