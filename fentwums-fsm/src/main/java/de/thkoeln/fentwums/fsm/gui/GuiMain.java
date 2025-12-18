/*
 * STDE - State Transition Diagram Editor
 *
 * 2011, 2012 Jan Montag, Andreas Schwenk
 * 2016         Georg Hartung
 * Component:   Gui
 * Class:       GuiMain
 * Created:     2011-10-28
 */
package de.thkoeln.fentwums.fsm.gui;

import de.thkoeln.fentwums.fsm.graph.Graph.GRAPH_TYPE;
import de.thkoeln.fentwums.fsm.gui.boundary.GuiPreferencesBoundary;
import de.thkoeln.fentwums.fsm.util.I18n;
import de.thkoeln.fentwums.fsm.util.ResourceLoader;
import de.thkoeln.fentwums.fsm.workflow.Workflow;
import lombok.Getter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * provides general GUI-functionality
 * 
 * @author Jan Montag
 */
public class GuiMain extends JFrame
{
    // *** ATTRIBUTES ***

    // associations
    @Getter
    protected Workflow workflow;
    @Getter
    private final Listener listener;
    protected GraphicsPanel graphicsPanel = new GraphicsPanel();

    protected JTabbedPane leftTableTabber;
    protected JTabbedPane rightTableTabber;
    protected GuiTableSignals guiTableSignals1;
    protected GuiTableVariables guiTableVariables1;
    protected GuiTableStates guiTableStates1;
    protected GuiTableSignals guiTableSignals2;
    protected GuiTableVariables guiTableVariables2;
    protected GuiTableStates guiTableStates2;
    
    protected GuiPreferences guiPreferences;

    // GUI-elements
    protected JScrollPane graphicsPanelScrollPane;

    private final JPanel tools;
    private final JPanel toolbar;

    protected JPanel clearLogPanel;

    protected JTextArea log;

    private JMenuBar menuBar;
    
    private GuiAbout guiAbout;

    protected JButton bNewFile, bOpenFile, bSaveFile, bUndo, bRedo, bDelete,
            bInsertState, bInsertStartNode, bInsertTransition, 
            bSelectAll, 
            bVerify, bSCXML, bGenerateC, bGenerateVHDL,
            bIntegrDeterm,
            bExportImage,
            bPreferences,
            bIncTransCtrlPoints, bDecTransCtrlPoints;

    protected JToggleButton btSelect, btInsert, btGrid;
    
    private final String title = "State Transition Diagram Editor   2011-2016 Jan Montag, Andreas Schwenk, Georg Hartung TH Köln Fak. IME Labor TI [V1.3]";

    // *** METHODS ***

    /**
    * adds a button with icons to the toolbar
    *
    * @param icon path to the main icon of the button
    * @param rolloverIcon path to the rollover-icon of the button
    * @param pressedIcon path to the pressed icon of the button
    * @param helpText tooltip text
    *
    * @return the JButton
    *
    * @author Jan Montag, Sebastian Wittlich
    */
    private JButton addButton(String icon, String rolloverIcon, String pressedIcon, String helpText)
    {
        JButton button = new JButton(ResourceLoader.loadImageIcon(icon));
        button.setRolloverEnabled(true);
        button.setRolloverIcon(ResourceLoader.loadImageIcon(rolloverIcon));
        button.setPressedIcon(ResourceLoader.loadImageIcon(pressedIcon));
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        button.setToolTipText(helpText);
        toolbar.add(button);
        return button;
    }

    /**
    * adds a toogle-button with icons to the toolbar
    *
    * @param selected sets button in false or true selection mode
    * @param icon path to the main icon of the button
    * @param rolloverIcon path to the rollover-icon of the button
    * @param pressedIcon path to the pressed icon of the button
    * @param helpText tooltip text
    *
    * @return a JToggleButton
    *
    * @author Jan Montag
    */
    private JToggleButton addToggleButton(boolean selected,
                                          String icon, String pressedIcon, String rolloverIcon, String helpText)
    {
        JToggleButton button = new JToggleButton(ResourceLoader.loadImageIcon(icon));
        button.setSelected(selected);
        button.setRolloverEnabled(true);
        button.setRolloverIcon(ResourceLoader.loadImageIcon(rolloverIcon));
        button.setSelectedIcon(ResourceLoader.loadImageIcon(pressedIcon));
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setToolTipText(helpText);
        toolbar.add(button);
        return button;
    }

    /**
    * adds a tool with an image-icon to tools
    *
    * @param box a box to insert the tool
    * @param img an icon for the tool
    * @param helpText some tooltips for the tool
    *
    * @return returns a JButton
    *
    * @author Jan Montag
    */
    private JButton addTool(Box box, String img, String helpText)
    {
        JButton button = new JButton(ResourceLoader.loadImageIcon(img));
        button.setBorder(new EmptyBorder(1,1,1,1));
        button.addActionListener(listener);
        button.setToolTipText(helpText);
        box.add(button);
        return button;
    }

    /**
    * Constructor of GuiMain
    *
    * @param width width of the mainwindow
    * @param height height of the mainwindow
    * @param macOSX boolean to set special settings for mac
    *
    * @author Jan Montag
    */
    public GuiMain(int width, int height, boolean macOSX)
    {
        super();
        setTitle(title);
        
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.setIconImage(ResourceLoader.loadImageIcon("favicon").getImage());
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        // about-gui
        guiAbout = new GuiAbout(this, ResourceLoader.loadImageIcon("about"));

        // mouse listener
        Mouse mouse = new Mouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        // keyboard listener
        Keyboard keyboard = new Keyboard(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(keyboard);

        workflow = new Workflow(this, graphicsPanel);

        leftTableTabber = new JTabbedPane();
        rightTableTabber = new JTabbedPane();
        
        // preferences
        guiPreferences = new GuiPreferences(this, new GuiPreferencesBoundary());
        
        // left table
        guiTableSignals1 = new GuiTableSignals(this);
        guiTableVariables1 = new GuiTableVariables(this);
        guiTableStates1 = new GuiTableStates(this);
        
        // right table
        guiTableSignals2 = new GuiTableSignals(this);
        guiTableVariables2 = new GuiTableVariables(this);
        guiTableStates2 = new GuiTableStates(this);

        //SplitPane Color
        Border border = new LineBorder(new Color(0xe3, 0xe3, 0xe3), 3);
        UIManager.put("SplitPaneDivider.border", border);

        listener = new Listener(this);
        this.addWindowListener(listener);

        graphicsPanel.addMouseListener(mouse);
        graphicsPanel.addMouseMotionListener(mouse);

        UIManager.getDefaults().put("Separator.foreground",
                new Color(0x40, 0x40, 0x40) );
        UIManager.getDefaults().put("Separator.background",
                new Color(0xe4, 0xe4, 0xe4) );
        UIManager.getDefaults().put("Separator.thickness", 2);

        java.awt.Container container = this.getContentPane();
        this.setLayout(new BorderLayout());


        // ****** TOP-PANEL *******

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());

        // ****** MENUBAR *******
        menuBar = new JMenuBar();

        menuBar.setBorder(BorderFactory.createLineBorder(new Color(0xb4, 0xb4, 0xb4)));
        menuBar.setBackground(new Color(0xb4, 0xb4, 0xb4));

        // MenuBar elements
        JMenu file = new JMenu(I18n.getString("menu.file"));
        JMenu help = new JMenu(I18n.getString("menu.help"));

        file.setBackground(new Color(0xb4, 0xb4, 0xb4));
        help.setBackground(new Color(0xb4, 0xb4, 0xb4));

        // File
        JMenuItem create = new JMenuItem(I18n.getString("menu.file.new"));
        final GuiMain gm = this;
        create.addActionListener(e -> listener.newFile());

        JMenuItem open = new JMenuItem(I18n.getString("menu.file.open"));
        open.addActionListener(e -> listener.openFile());

        JMenuItem save = new JMenuItem(I18n.getString("menu.file.save"));
        save.addActionListener(e -> listener.saveFile());

        JMenuItem saveAS = new JMenuItem(I18n.getString("menu.file.saveAs"));
        saveAS.addActionListener(e -> listener.saveFileAs());

        JMenuItem export = new JMenuItem(I18n.getString("menu.file.exportImage"));
        export.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                listener.exportAsImage();
            }
        });

        JMenuItem exit = new JMenuItem(I18n.getString("menu.file.exit"));
        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int i = confirmationDialog(I18n.getString("dialog.exit.saveConfirmation"));
                if(i == 0)
                {
                    listener.saveFileAs();
                }
                else if(i == 1)
                {
                    System.exit(0);
                }
                System.exit(0);
            }
        });

        // Help
        JMenuItem about = new JMenuItem(I18n.getString("menu.help.about"));
        about.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                guiAbout.setVisible(true);
            }
        });
        
        // add MenuBar elements
        menuBar.add(file);
        menuBar.add(help);

        // add sub-MenuBar elements
        file.add(create);
        file.add(open);
        file.add(new JSeparator());
        file.add(save);
        file.add(saveAS);
        file.add(export);
        file.add(new JSeparator());
        file.add(exit);

        help.add(about);

        this.setJMenuBar(menuBar);

        // ****** TOOLBAR *******
        toolbar = new JPanel();

        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBackground(new Color(0xb4, 0xb4, 0xb4));
        toolbar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        toolbar.add(Box.createHorizontalStrut(1));
        bNewFile = addButton("bNewFile", "bNewFile_rollover", "bNewFile_pressed", I18n.getString("toolbar.new"));
        bOpenFile = addButton("bOpenFile", "bOpenFile_rollover", "bOpenFile_pressed", I18n.getString("toolbar.open"));
        bSaveFile = addButton("bSaveFile", "bSaveFile_rollover", "bSaveFile_pressed", I18n.getString("toolbar.save"));
        toolbar.add(Box.createHorizontalStrut(10));
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(10));

        bExportImage = addButton("bExportAsImage", "bExportAsImage_rollover", "bExportAsImage_pressed", I18n.getString("toolbar.exportImage"));
        
        toolbar.add(Box.createHorizontalStrut(10));
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        /*
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(15));        
        bRedo = addButton("/Resources/bUndo.png", "/Resources/bUndo_rollover.png", "/Resources/bUndo_pressed.png", "Rückgänig");
        bUndo = addButton("/Resources/bRedo.png", "/Resources/bRedo_rollover.png", "/Resources/bRedo_pressed.png", "Wiederherstellen");
        toolbar.add(Box.createHorizontalStrut(10));
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        */
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(15));
        btSelect = addToggleButton(false, "bSelect", "bSelect_pressed", "bSelect_rollover", I18n.getString("toolbar.mode.select"));
        btInsert = addToggleButton(true, "bInsert", "bInsert_pressed", "bInsert_rollover", I18n.getString("toolbar.mode.insert"));
        toolbar.add(Box.createHorizontalStrut(15));
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(10));
        bDelete = addButton("bDelete", "bDelete_rollover", "bDelete_pressed", I18n.getString("toolbar.delete"));
        toolbar.add(Box.createHorizontalStrut(10));
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(15));
        bSelectAll = addButton("bSelectAll", "bSelectAll_rollover", "bSelectAll_pressed", I18n.getString("toolbar.selectAll"));
        btGrid = addToggleButton(true, "bGrid", "bGrid_pressed", "bGrid_rollover", I18n.getString("toolbar.grid"));
        toolbar.add(Box.createHorizontalStrut(10));
        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(15));
        bPreferences = addButton("bPreferences", "bPreferences_rollover", "bPreferences_pressed", I18n.getString("toolbar.preferences"));
        toolbar.add(Box.createHorizontalStrut(10));

        sep = new JSeparator(JSeparator.VERTICAL);
        sep.setMaximumSize(new Dimension(2,26));
        toolbar.add(sep);
        toolbar.add(Box.createHorizontalStrut(15));
        bIncTransCtrlPoints = addButton("bIncCtrlPoints",
                "bIncCtrlPoints_rollover",
                "bIncCtrlPoints_pressed",
                I18n.getString("toolbar.transition.addPoint"));

        bDecTransCtrlPoints = addButton("bDecCtrlPoints",
                "bDecCtrlPoints_rollover",
                "bDecCtrlPoints_pressed",
                I18n.getString("toolbar.transition.removePoint"));
        
        
        // ****** HORIZONTAL_LINE *******
        JPanel hzLine = new JPanel();

        hzLine.setLayout(new BoxLayout(hzLine, BoxLayout.Y_AXIS));
        hzLine.setBackground(new Color(0xe3, 0xe3, 0xe3));
        JSeparator hzSep = new JSeparator(JSeparator.HORIZONTAL);
        hzLine.add(hzSep);
        hzLine.add(Box.createVerticalStrut(3));

        topPanel.add(toolbar, BorderLayout.CENTER);
        topPanel.add(hzLine, BorderLayout.SOUTH);

        // ****** SIDEBAR *******
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        sidebar.setBackground(new Color(0xe3, 0xe3, 0xe3));

        // ****** TOOLS *******
        tools = new JPanel();
        tools.setLayout(new BoxLayout(tools, BoxLayout.Y_AXIS));


        JPanel panel = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMinimumSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(2500, 21);
            }
            @Override
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setPaint(new GradientPaint(1, 0, (new Color(0xe7, 0xe7, 0xe7)), 1, 21, (new Color(0xe7, 0xe7, 0xe7)), false));
                g2d.fillRect(0, 0, getSize().width,getSize().height/2);
                g2d.setPaint(new GradientPaint(1, 11, (new Color(0xd4, 0xd4, 0xd4)), 1, 21,(new Color(0xd4, 0xd4, 0xd4)) , false));
                g2d.fillRect(0,getSize().height/2,getSize().width,getSize().height);
                g2d.setColor(Color.white);
                g2d.drawLine(0, 0, getSize().width, 0);
                g2d.drawLine(0, 0, getSize().width, 0);
                g2d.drawLine(getSize().width-1, 0, getSize().width-1, getSize().height);
                g2d.drawLine(0, 1, 0, getSize().height);
                g2d.setColor(new Color(0x88, 0x8b, 0x94));
                g2d.drawLine(0, getSize().height-1, getSize().width, getSize().height-1);

                g2d.setColor(Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 11);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics(font);

                String str = "Tools";
                int strWidth = fm.stringWidth(str);

                g2d.drawString(str, getSize().width/2-strWidth/2, fm.getHeight());
            }


        };
        panel.setLayout(new FlowLayout());

        tools.add(panel);
        tools.add(Box.createVerticalStrut(8));
        Box box = Box.createHorizontalBox();

        bInsertStartNode = addTool(box, "bInsertMooreStartNode", I18n.getString("toolbar.insert.startNode"));
        bInsertTransition = addTool(box, "bInsertMooreTransition", I18n.getString("toolbar.insert.mooreTransition"));
        tools.add(box);
        bInsertState = addTool(box, "bInsertMooreState", I18n.getString("toolbar.insert.mooreState"));

        tools.add(box);

        tools.add(Box.createVerticalStrut(10));

        Border grayLine = BorderFactory.createLineBorder(new Color(0x88, 0x8b, 0x94));
        tools.setBorder(grayLine);

        sidebar.add(tools);

        // ****** BUILD *******
        sidebar.add(Box.createVerticalStrut(4));

        JPanel buildPanel = new JPanel();
        buildPanel.setLayout(new BoxLayout(buildPanel, BoxLayout.Y_AXIS));
        
        panel = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMinimumSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(2500, 21);
            }

            @Override
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setPaint(new GradientPaint(1, 0, (new Color(0xe7, 0xe7, 0xe7)), 
                        1, 21, (new Color(0xe7, 0xe7, 0xe7)), false));
                g2d.fillRect(0, 0, getSize().width,getSize().height/2);
                g2d.setPaint(new GradientPaint(1, 11, (new Color(0xd4, 0xd4, 0xd4)), 
                        1, 21,(new Color(0xd4, 0xd4, 0xd4)) , false));
                g2d.fillRect(0,getSize().height/2,getSize().width,getSize().height);
                g2d.setColor(Color.white);
                g2d.drawLine(0, 0, getSize().width, 0);
                g2d.drawLine(0, 0, getSize().width, 0);
                g2d.drawLine(getSize().width-1, 0, getSize().width-1, getSize().height);
                g2d.drawLine(0, 1, 0, getSize().height);
                g2d.setColor(new Color(0x88, 0x8b, 0x94));
                g2d.drawLine(0, getSize().height-1, getSize().width, getSize().height-1);

                g2d.setColor(Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 11);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics(font);

                String str = "Build";
                int strWidth = fm.stringWidth(str);

                g2d.drawString(str, getSize().width/2-strWidth/2, fm.getHeight());
            }


        };
        panel.setLayout(new FlowLayout());

        buildPanel.add(panel);
        if(!macOSX)
        {
            buildPanel.setBackground(new Color(0xec, 0xe9, 0xd8));
        }

        box = Box.createHorizontalBox();
        box.setBorder(BorderFactory.createEmptyBorder());

        bVerify = addTool(box, "verify", I18n.getString("toolbar.tool.verify"));
        bIntegrDeterm = addTool(box, "check_integr_determ", I18n.getString("toolbar.tool.checkIntegrity"));
        bSCXML = addTool(box, "scxml", I18n.getString("toolbar.tool.exportSCXML"));
        bGenerateC = addTool(box, "generate_C", I18n.getString("toolbar.tool.generateC"));
        bGenerateVHDL = addTool(box, "generate_VHDL", I18n.getString("toolbar.tool.generateVHDL"));
        
        buildPanel.add(Box.createVerticalStrut(8));
        buildPanel.add(box);
        buildPanel.add(Box.createVerticalStrut(8));
        
        grayLine = BorderFactory.createLineBorder(new Color(0x88, 0x8b, 0x94));
        buildPanel.setBorder(grayLine);

        sidebar.add(buildPanel);

        // ****** LOG *******
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));

        panel = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMinimumSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(2500, 21);
            }

            @Override
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setPaint(new GradientPaint(1, 0, (new Color(0xe7, 0xe7, 0xe7)), 
                        1, 21, (new Color(0xe7, 0xe7, 0xe7)), false));
                g2d.fillRect(0, 0, getSize().width,getSize().height/2);
                g2d.setPaint(new GradientPaint(1, 11, (new Color(0xd4, 0xd4, 0xd4)), 
                        1, 21,(new Color(0xd4, 0xd4, 0xd4)) , false));
                g2d.fillRect(0,getSize().height/2,getSize().width,getSize().height);

                g2d.setColor(Color.white);
                g2d.drawLine(0, 1, getSize().width, 1); //vertical
                g2d.drawLine(getSize().width-1-1, 0, getSize().width-1-1, getSize().height);
                g2d.drawLine(1, 1, 1, getSize().height);

                g2d.setColor(new Color(0x88, 0x8b, 0x94));
                g2d.drawLine(0, 0, getSize().width, 0); //vertical
                g2d.drawLine(0, getSize().height-1, getSize().width, getSize().height-1); //vertical
                g2d.drawLine(getSize().width-1, 0, getSize().width-1, getSize().height);
                g2d.drawLine(0, 0, 0, getSize().height);

                g2d.setColor(Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 11);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics(font);

                String str = "Log";
                int strWidth = fm.stringWidth(str);

                g2d.drawString(str, getSize().width/2-strWidth/2, fm.getHeight());
            }


        };
        panel.setLayout(new FlowLayout());
        logPanel.add(Box.createVerticalStrut(4));
        logPanel.add(panel);
        logPanel.setBackground(new Color(0xe3, 0xe3, 0xe3));

        box = Box.createHorizontalBox();

        log = new JTextArea("");
        log.setFont(new Font("Courier New", Font.BOLD, 12));
        log.setEditable(false);

        JScrollPane sp = new JScrollPane(log);
        sp.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(0x88, 0x8b, 0x94)));

        box.add(sp);
        logPanel.add(box);
        
        // clear log
        clearLogPanel = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMinimumSize()
            {
                return new Dimension(250, 21);
            }

            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(2500, 21);
            }

            @Override
            public void paint(Graphics g)
            {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setPaint(new GradientPaint(1, 0, (new Color(0xe7, 0xe7, 0xe7)), 
                        1, 21, (new Color(0xe7, 0xe7, 0xe7)), false));
                g2d.fillRect(0, 0, getSize().width,getSize().height/2);
                g2d.setPaint(new GradientPaint(1, 11, (new Color(0xd4, 0xd4, 0xd4)), 
                        1, 21,(new Color(0xd4, 0xd4, 0xd4)) , false));
                g2d.fillRect(0,getSize().height/2,getSize().width,getSize().height);

                g2d.setColor(Color.white);
                g2d.drawLine(0, 1, getSize().width, 1); //vertical
                g2d.drawLine(getSize().width-1-1, 0, getSize().width-1-1, getSize().height);
                g2d.drawLine(1, 1, 1, getSize().height);

                g2d.setColor(new Color(0x88, 0x8b, 0x94));
                g2d.drawLine(0, 0, getSize().width, 0); //vertical
                g2d.drawLine(0, getSize().height-1, getSize().width, getSize().height-1); //vertical
                g2d.drawLine(getSize().width-1, 0, getSize().width-1, getSize().height);
                g2d.drawLine(0, 0, 0, getSize().height);

                g2d.setColor(Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 11);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics(font);

                String str = "Clear Log";
                int strWidth = fm.stringWidth(str);

                g2d.drawString(str, getSize().width/2-strWidth/2, fm.getHeight());
            }


        };
        clearLogPanel.setLayout(new FlowLayout());
        clearLogPanel.addMouseListener(mouse);
        logPanel.add(clearLogPanel);
        

        sidebar.add(logPanel);

        // ****** LEFT-PANEL *******
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setBackground(new Color(0xe3, 0xe3, 0xe3));
        leftPanel.add(Box.createHorizontalStrut(2));
        leftPanel.setBorder(new EmptyBorder(0,0,0,0));
        leftPanel.add(sidebar);

        // ****** MAIN-WINDOW *******
        JPanel mainWindow = new JPanel();
        BorderLayout mainWindowBorderLayout = new BorderLayout();
        mainWindow.setLayout(mainWindowBorderLayout);
        // tables
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(1,2));
        tablePanel.setBackground(new Color(0xe3, 0xe3, 0xe3));
        tablePanel.setBorder(new EmptyBorder(0,0,0,0));

        leftTableTabber.add(I18n.getString("tab.signals"), guiTableSignals1);
        leftTableTabber.add(I18n.getString("tab.variables"), guiTableVariables1);
        leftTableTabber.add(I18n.getString("tab.states"), guiTableStates1);

        rightTableTabber.add(I18n.getString("tab.signals"), guiTableSignals2);
        rightTableTabber.add(I18n.getString("tab.variables"), guiTableVariables2);
        rightTableTabber.add(I18n.getString("tab.states"), guiTableStates2);

        //default
        rightTableTabber.setSelectedIndex(1);
        
        Boolean ignoreEvent = false;

        leftTableTabber.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int i = leftTableTabber.getSelectedIndex();
                if(rightTableTabber.getSelectedIndex() == i)
                    rightTableTabber.setSelectedIndex((rightTableTabber.getSelectedIndex()+1)%3);
            }

        });

        rightTableTabber.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e) {
                int i = rightTableTabber.getSelectedIndex();
                if(leftTableTabber.getSelectedIndex() == i)
                    leftTableTabber.setSelectedIndex((leftTableTabber.getSelectedIndex()+1)%3);
            }

        });


        tablePanel.add(leftTableTabber);
        tablePanel.add(rightTableTabber);

        // graphics-panel with scrollbar
        graphicsPanelScrollPane = new JScrollPane(graphicsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(400,300); /* ex 500x350 */
            }
            @Override
            public Dimension getMinimumSize()
            {
                return new Dimension(400,300); /* ex 500x350 */
            }
        };
        graphicsPanelScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        graphicsPanelScrollPane.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, new Color(0x88, 0x8b, 0x94)));

        // Splitpane (GraphicsPanel <=> TablePanel)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           graphicsPanelScrollPane, tablePanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(new EmptyBorder(0,0,0,0));
        splitPane.setDividerSize(3);
        mainWindow.add(splitPane);

        container.add(topPanel, BorderLayout.NORTH);

        // Splitpane (LeftPanel <=> MainWindow)
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, mainWindow);
        splitPane.setResizeWeight(0.2);
        splitPane.setBorder(new EmptyBorder(0,0,0,0));
        splitPane.setDividerSize(3);
        this.add(splitPane);

        this.setVisible(true);

        NewFileDialog newFileDialogTmp = new NewFileDialog(this,this);
    }

    public static void main(String[] args)
    {
        boolean macOSX = false;
        String osname = System.getProperty("os.name");

        if(osname.contains("Mac")) {
            macOSX = true;
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.brushMetalLook", "true");
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { System.out.println("error: setLookAndFeel(..) failed"); }

        GuiMain main = new GuiMain(1000, 650, macOSX);
    }


    /**
    * opens a confirmation dialog
    *
    * @param question yes-no-question
    *
    * @return an integer depending on the answer "yes", "no" or "cancel"
    *
    * @author Jan Montag
    */
    public int confirmationDialog(String question) {
        Object[] options = {
                I18n.getString("dialog.button.yes"),
                I18n.getString("dialog.button.no"),
                I18n.getString("dialog.button.cancel")
        };
            int response = JOptionPane.showOptionDialog(this,
                    question,
                    I18n.getString("dialog.title.confirm"), JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            switch (response)
            {

                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2, -1:
                    return -1;
                default:
                    JOptionPane.showMessageDialog(null, I18n.getString("dialog.error.unexpected") + " " + response);
            }
            return -1;
    }

    /**
    * changes tools depending on selecting mealy or moor
    *
    * @author Jan Montag
    */
    public void changeTools()
    {
        if(workflow.getGraph().getGraphType() == GRAPH_TYPE.MOORE)
        {
            bInsertStartNode.setIcon(ResourceLoader.loadImageIcon("bInsertMooreStartNode"));
            bInsertStartNode.setToolTipText(I18n.getString("toolbar.insert.startNode"));

            bInsertTransition.setIcon(ResourceLoader.loadImageIcon("bInsertMooreTransition"));
            bInsertTransition.setToolTipText(I18n.getString("toolbar.insert.mooreTransition"));

            bInsertState.setIcon(ResourceLoader.loadImageIcon("bInsertMooreState"));
            bInsertState.setToolTipText(I18n.getString("toolbar.insert.mooreState"));
        }

        if(workflow.getGraph().getGraphType() == GRAPH_TYPE.MEALY)
        {
            bInsertStartNode.setIcon(ResourceLoader.loadImageIcon("bInsertMealyStartNode"));
            bInsertStartNode.setToolTipText(I18n.getString("toolbar.insert.startNode"));

            bInsertTransition.setIcon(ResourceLoader.loadImageIcon("bInsertMealyTransition"));
            bInsertTransition.setToolTipText(I18n.getString("toolbar.insert.mealyTransition"));

            bInsertState.setIcon(ResourceLoader.loadImageIcon("bInsertMealyState"));
            bInsertState.setToolTipText(I18n.getString("toolbar.insert.mealyState"));
        }

        tools.repaint();
    }

    /**
    * refreshs the GraphicsPanelScrollPane
    *
    * @author Jan Montag
    */
    public void refreshGraphicsPanelScrollPane()
    {
        graphicsPanelScrollPane.getViewport().setViewPosition(new Point(1, 0));
        graphicsPanelScrollPane.getViewport().setViewPosition(new Point(0, 0));
    }


    /**
    * change the title of the project
    *
    * @param projectName the name of the project
    *
    * @author Jan Montag
    */
    public void changeTitleByProjectName(String projectName)
    {
        setTitle(projectName + " - " + title);
    }

    /**
    * clear the LogText
    *
    * @author Jan Montag
    */
    public void clearLogText()
    {
        log.setText("");
    }
}
