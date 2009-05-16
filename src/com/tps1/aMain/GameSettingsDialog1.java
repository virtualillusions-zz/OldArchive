package com.tps1.aMain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.jme.system.GameSettings;
import com.tps1.util.ImageCache;
import com.tps1.util.SysInfo;

//import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

//import org.lwjgl.opengl.Display;

/**A dialog box for configuring the game settings.
 *  
 * @author Kyle Williams
 */
public class GameSettingsDialog1 extends JFrame {

    private static Logger logger = Logger.getLogger(GameSettingsDialog.class.getName());
    private static final int FRAME_WIDTH = 573;
    
    /**The game settings to set.*/
    private GameSettings settings;
    
    /**Determines if the dialog is active.*/
    private boolean active = true;

    /**
     * Lock for waiting till the window closes
     */
    private Object openLock = new Object();
    
    /**Determines if the game should start.*/
    private boolean startGame = false;

    /**Constructor - Creates a dialog for setting up the game.
     * 
     * @param settings The game settings to set.
     */
    public GameSettingsDialog1(GameSettings settings) {
        super("Launcher - "+SysInfo.GameName);
        
        if (settings == null) {
            throw new NullPointerException("Settings can not be null.");
        }

        this.settings = Game.getSettings();
        setupFrame();
    }

    /**
     * Waits until the dialog box is closed.
     */
    public void waitFor() throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new UnsupportedOperationException("Cannot be called from gui thread");
        }

        while (isOpen()) {
            synchronized (openLock){
                openLock.wait();
            }
        }
    }

    /**
     * Determines if the dialog box is open.
     * 
     * @return True if it is open.
     */
    public boolean isOpen() {
        return active;
    }

    /**Sets up the main frame.*/
    private void setupFrame() {
        logger.fine("Loading the configuration dialog...");

        //SETUP JFRAME		
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });

        setPreferredSize(new Dimension(FRAME_WIDTH, 350));
        setResizable(false);
        setLocationByPlatform(true);

        //SETUP COMPONENTS OF FRAME		
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

         
       JPanel imagePl = new JPanel() {
            private static final long serialVersionUID = 6279143708867242615L;
           //private Image bgImg =ImageCache.retrieveCachedImage("/com/tps1/data/images/logo.jpg");
            private Image bgImg =Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/tps1/data/images/logo.jpg"));
            @Override
            public void paint(Graphics g) {
                g.drawImage(bgImg, 0, 0, bgImg.getWidth(this), bgImg.getHeight(this), this);
                // Unable To locate Image. 
              if (bgImg == null){logger.severe("ALERT, Missing Image!!");}
            }
        };


        JPanel graphicsPa = new JPanel();
        JPanel audioPa = new JPanel();
        JPanel sysPa = new JPanel();
        JPanel commandPa = new JPanel();
        JTabbedPane tabs = new JTabbedPane();

        //Add panels to tabs
        tabs.addTab("Graphics", graphicsPa);
        tabs.addTab("Audio", audioPa);
        tabs.addTab("SystmInfo", sysPa);

        graphicsPa.setLayout(new GridBagLayout());
        graphicsPa.setPreferredSize(new Dimension(FRAME_WIDTH, 75));
        GridBagConstraints c = new GridBagConstraints();

        JLabel selectLb = new JLabel("Display Mode: ");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        graphicsPa.add(selectLb, c);
///////////////////////////////////////////////////////////////////////////////
        //SETUP GRAPHICS PANEL
        //setup up display modes
          JComboBox listCb = new JComboBox();
        listCb.setSize(60, 20);
        listCb.setPreferredSize(new Dimension(150, 20));
       DisplayMode displays[] = gd.getDisplayModes();
        for (int i = 0; i < displays.length; i++) {
           listCb.addItem(new DisplayWrapper(displays[i]));      
        }
        listCb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem() instanceof DisplayWrapper) {
                    DisplayMode mode = ((DisplayWrapper) e.getItem()).mode;
                    settings.setDepth(mode.getBitDepth());
                    settings.setHeight(mode.getHeight());
                    settings.setWidth(mode.getWidth());
                    settings.setFrequency(mode.getRefreshRate());
                }
            }
        });   
        listCb.setSelectedItem(new DisplayWrapper(gd.getDisplayMode()));
       
        
        c.insets = new Insets(0, 0, 0, 20);
        c.gridx = 1;
        graphicsPa.add(listCb, c);      

        //setup full screen check box		
        JLabel fullLb = new JLabel("Full Screen: ");

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 2;
        graphicsPa.add(fullLb, c);

        JCheckBox cb = new JCheckBox();
        cb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                settings.setFullscreen(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        if (settings.isFullscreen()==true)
    	{cb.setSelected(true);}
    	else
    	{cb.setSelected(false);}

        c.gridx = 3;
        graphicsPa.add(cb, c);

        //setup vertical sync check box
        JLabel syncLb = new JLabel("Vertical Sync: ");

        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 4;
        graphicsPa.add(syncLb, c);

        cb = new JCheckBox();
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                settings.setVerticalSync(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        if (settings.isVerticalSync()==true)
    	{cb.setSelected(true);}
    	else
    	{cb.setSelected(false);}

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 5;
        graphicsPa.add(cb, c);
//////////////////////////////////////////////////////////////////////////////////
        //SETUP THE AUDIO PANEL
        audioPa.setLayout(new GridBagLayout());
        audioPa.setPreferredSize(new Dimension(FRAME_WIDTH, 75));
        //setup music check box
        JLabel msxLb = new JLabel("Enable Music: ");

        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 0;
        audioPa.add(msxLb, c);

        cb = new JCheckBox();
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	settings.setMusic(e.getStateChange() == ItemEvent.SELECTED);
            	}
        });
        if (settings.isMusic()==true)
    	{cb.setSelected(true);}
    	else
    	{cb.setSelected(false);} 
        
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        audioPa.add(cb, c);
        //setup sfx check box
        JLabel sfxLb = new JLabel("Enable SFX: ");

        c.insets = new Insets(0, 50, 0, 0);
        c.gridx = 2;
        audioPa.add(sfxLb, c);

        cb = new JCheckBox();
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                settings.setSFX(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        if (settings.isSFX()==true)
    	{cb.setSelected(true);}
    	else
    	{cb.setSelected(false);}

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 3;
        audioPa.add(cb, c);
      //SETUP THE System Info//////////////////////////////////////////////////
        sysPa.setLayout(new GridBagLayout());
        sysPa.setPreferredSize(new Dimension(FRAME_WIDTH, 75));
        //setup System Info
        JLabel sysLb = new JLabel("==System==");
        JLabel sysLb2 = new JLabel("OS Architecture: "+System.getProperty("os.arch"));
        JLabel sysLb3 = new JLabel("OS Name: "+System.getProperty("os.name"));
        JLabel sysLb4 = new JLabel("OS Version: "+System.getProperty("os.version"));
        JLabel sysLb5 = new JLabel("Locale: "+Locale.getDefault().toString());
        JLabel sysLb6 = new JLabel("Processor count: "+Runtime.getRuntime().availableProcessors());
        JLabel sysLb7 = new JLabel("Free memory: "+(Runtime.getRuntime().freeMemory()/1048576)+" MB");
        JLabel sysLb8 = new JLabel("Total memory: "+(Runtime.getRuntime().totalMemory()/1048576)+" MB");
        JLabel sysLb9 = new JLabel("Max memory: "+(Runtime.getRuntime().maxMemory()/1048576)+" MB");
        
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 1;
        sysPa.add(sysLb, c);
        c.gridy = 1;
        sysPa.add(sysLb2, c);
        c.gridy = 2;
        sysPa.add(sysLb3, c);
        c.gridy = 3;
        sysPa.add(sysLb4, c);
        c.gridy = 4;
        sysPa.add(sysLb5, c);
        c.gridy = 1;
        c.gridx = 2;
        sysPa.add(sysLb6, c);
        c.gridy = 2;
        sysPa.add(sysLb7, c);
        c.gridy = 3;
        sysPa.add(sysLb8, c);
        c.gridy = 4;
        sysPa.add(sysLb9, c);
        
      //setup Virtual Machine Info
        JLabel vsLb = new JLabel("==Virtual Machine==");
        JLabel vsLb2 = new JLabel("Runtime: "+System.getProperty("java.runtime.name"));
        JLabel vsLb3 = new JLabel("Vendor: "+System.getProperty("java.vendor"));
        JLabel vsLb4 = new JLabel("Version: "+System.getProperty("java.runtime.version"));
        
        c.gridx = 3;
        c.gridy=0;
        sysPa.add(vsLb, c);
        c.gridy = 1;
        sysPa.add(vsLb2, c);
        c.gridy = 2;
        sysPa.add(vsLb3, c);
        c.gridy = 3;
        sysPa.add(vsLb4, c);

        


        //SETUP THE COMMAND PANEL
        commandPa.setLayout(new FlowLayout(FlowLayout.RIGHT));
        commandPa.setPreferredSize(new Dimension(FRAME_WIDTH, 35));

        JButton startz = new JButton("Start");
        startz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame = true;
                closeDialog();
            }
        });
        commandPa.add(startz);

        JButton quitz = new JButton("Quit");
        quitz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });
        commandPa.add(quitz);

        //finalize the frame by adding panenls to it
        getContentPane().setLayout(new BorderLayout());

        JPanel subPanel = new JPanel(new BorderLayout());
        subPanel.add(tabs);
        subPanel.add(commandPa, BorderLayout.SOUTH);

        add(imagePl);
        add(subPanel, BorderLayout.SOUTH);
    }

    /**
     * Configures the game settings.
     */
    public void configure() {
        pack();
        setVisible(true);
    }

    /**
     * Closes the dialog box.
     */
    public void closeDialog() {
        setVisible(false);
        dispose();
        active = false;
        synchronized (openLock){
            openLock.notifyAll();
        }
        logger.fine("Configuration Finished!");
    }

    /**Determines if the game is allowed to initialize.
     * 
     * @return True if the game is allowed to initialize.
     */
    public boolean isInitGameAllowed() {
        return startGame;
    }

    /**A simple wrapper for a <code>DisplayMode</code>. 
     * 
     * @author Joshua Montgomery
     * @version 1.0.0
     * @created Jul 28, 2008
     */
    private class DisplayWrapper {

        /**The wrapped <code>DisplayMode</code>.*/
        DisplayMode mode;

        /**Constructor - Constructs a wrapper for a <code>DisplayMode</code>.
         * 
         * @param mode The <code>DisplayMode</code> to wrap.
         */
        public DisplayWrapper(DisplayMode mode) {
            this.mode = mode;
        }

        public boolean equals(Object o) {
            if (o instanceof DisplayWrapper) {
                return mode.equals(((DisplayWrapper) o).mode);
            }
            return mode.equals(o);
        }
   
        public String toString() {
            return (mode.getWidth() + "x" + mode.getHeight() +
                    "x" + mode.getBitDepth() + " @" + mode.getRefreshRate());
        }
    }
}
