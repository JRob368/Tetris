/*
* TCSS 305 – Autumn 2016
* Assignment 6 – Tetris
*/
package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

/**
 * The menu for the Tetris program.
 * @author James Roberts
 * @version 2 Dec 2016
 */
public class TetrisMenu extends JMenuBar {

    /** The Serial ID #. */
    private static final long serialVersionUID = 6523545150132007076L;

    /** Constructor for the Tetris Menu. 
     * @param theTimer the program's Timer object
     * @param theGamePanel the program's GamePanel
     */
    public TetrisMenu(final Timer theTimer, final GamePanel theGamePanel) {
        super();
        final MenuMouseListener listener = new MenuMouseListener(theTimer, theGamePanel);
        buildSettingsMenu(theGamePanel, listener);     
        buildAboutMenu(listener);
    }
    
    /**
     * Creates the game's settings menu which allows the user to choose the board
     * size of a game. 
     * @param theGamePanel Visual representation of the game. 
     * @param theListener Listener that pauses the game when menu is pressed.
     */
    private void buildSettingsMenu(final GamePanel theGamePanel, 
                                   final MenuMouseListener theListener) {
        final JMenu settingsMenu = new JMenu("Settings");     
        
        
        settingsMenu.add(createSlider("Set Game Width", "WidthSetting", 
                                      GamePanel.DEFAULT_BOARD_WIDTH));
        settingsMenu.add(createSlider("Set Game Height", "HeightSetting", 
                                      GamePanel.DEFAULT_BOARD_HEIGHT));
        settingsMenu.addSeparator();
        
        final ButtonGroup btnGrp = new ButtonGroup();
        final JRadioButtonMenuItem monoBtn = new JRadioButtonMenuItem("MonoChrome Scheme");
        monoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                theGamePanel.isMonochrome(true);
            }
        });
        monoBtn.setSelected(true);
        btnGrp.add(monoBtn);
        final JRadioButtonMenuItem tradBtn = new JRadioButtonMenuItem("Traditional Scheme");
        tradBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                theGamePanel.isMonochrome(false);
            }
        });
        btnGrp.add(tradBtn);
        settingsMenu.add(monoBtn);
        settingsMenu.add(tradBtn);
        settingsMenu.addSeparator();
        
        final JCheckBoxMenuItem hardCheckBox = new JCheckBoxMenuItem("Hard Mode");
        hardCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                firePropertyChange("HardMode", null, hardCheckBox.isSelected());
                                           
            }
        });
        settingsMenu.add(hardCheckBox);
        
        settingsMenu.addMouseListener(theListener);
        add(settingsMenu);

    }
    
    
    /**
     * Creates a JMenu item of the passed name containing a slider that fires
     * the selected number as a property change event using the passed property name.
     * @param theMenuName the name of the menu.
     * @param thePropertyName the name of the property.
     * @param theDefaultVal value the slider should be set at when created.
     * @return JMenu with a slider.
     */
    private JMenu createSlider(final String theMenuName, final String thePropertyName, 
                               final int theDefaultVal) {
        final JMenu slideMenu = new JMenu(theMenuName);
        final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 5, 35, theDefaultVal);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent theEvent) {
                final int value = slider.getValue();
                if (value >= 0) {
                    firePropertyChange(thePropertyName, null, value);
                }
            }
        });
        slideMenu.add(slider);
        
        return slideMenu;
    }
    /**
     * Builds the game's about menu which provides information about scoring and about
     * the game's hard mode.
     * @param theListener Listener that pauses the game when menu is pressed. 
     */
    private void buildAboutMenu(final MenuMouseListener theListener) {
        final JMenu aboutMenu = new JMenu("About");
        final JMenuItem scoringBtn = new JMenuItem("Scoring...");
        scoringBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(null, 
                                  "The value of each line cleared is worth 100 x "
                                 + "the current level.\n" 
                                 + "The Timer starts with a delay of 850 m/s.\n"
                                 + "With each level cleared the Timer decreases by .25 * " 
                                 + "the number of levels cleared until it reaches 100 m/s.",
                                  "Scoring", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        aboutMenu.add(scoringBtn);
        
        final JMenuItem hardModeBtn = new JMenuItem("Hard Mode...");
        hardModeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(null, 
                                  "Hard mode can be selected by checking the box in the"
                                  + "Settings Menu.\nIn Hardmode each line cleared is worth"
                                  + " twice as many points!\nDuring each Even level, the left "
                                  + "and right arrow keys reverse as well as the up and down"
                                  + " arrow keys.",
                                  "Hard Mode", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        aboutMenu.add(hardModeBtn);
        
        aboutMenu.addMouseListener(theListener);
        add(aboutMenu);
    }
    
    /**
     * Mouse Listener that pauses the Tetris Game. 
     * @author James Roberts
     * @version 9 Dec. 2016
     */
    private final class MenuMouseListener extends MouseInputAdapter {
        /** The game's Timer. */
        private final Timer myTimer;
        /** The visual representation of the game. */
        private final GamePanel myGamePanel;
        
        /** 
         * Constructor for the MenuMouseListener. 
         * @param theTimer the game's Timer. 
         * @param theGamePanel a visual representation of the game. 
         */
        MenuMouseListener(final Timer theTimer, final GamePanel theGamePanel) {
            super();
            myTimer = theTimer;
            myGamePanel = theGamePanel;
        }
        
        /**
         * Stops the Timer and displays a paused screen on the game.
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(final MouseEvent theEvent) {
            if (myTimer.isRunning()) {
                myTimer.stop();
                myGamePanel.isPaused(true);
            }
        }
    }
    
}
