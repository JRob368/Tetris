/*
* TCSS 305 – Autumn 2016
* Assignment 6 – Tetris
*/
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


import javax.swing.JPanel;

/**
 * Panel representing the Tetris game's controls.  
 * @author James Roberts
 * @version 2 December 2016
 */
public class InformationPanel extends JPanel {
    /** The serial ID #.*/
    private static final long serialVersionUID = 4914587160231691701L;
    /** Length of the panel's side. */
    private static final int PANEL_SIZE = 200;
    /** Size of the panel's title font. */
    private static final int TITLE_FONT_SIZE = 18;
    /** Size of the panel's font. */
    private static final int FONT_SIZE = 15;
    /** X location of the panel's text. */
    private static final int TEXT_X_LOC = 2;
    /** Y location of the panel's first line of text. */
    private static final int START_Y_LOC = 12;
    /** An arrow pointing left. */
    private static final String LEFT_ARROW = "\u2190";
    /** An arrow pointing right. */
    private static final String RIGHT_ARROW = "\u2192";
    /** An arrow pointing up. */
    private static final String UP_ARROW = "\u2191";
    /** An arrow pointing down. */
    private static final String DOWN_ARROW = "\u2193";
    
    /** Key to move a piece left. */
    private String myLeftControl;
    /** Key to move a piece right. */
    private String myRightControl;
    /** Key to move a piece down. */
    private String myDownControl;
    /** Key to rotate a piece. */
    private String myRotateControl;
    /** Key to drop a piece. */
    private String myDropControl;
    /** Key to pause the game. */
    private String myPauseControl;
    /** Key to resume the game. */
    private String myResumeControl;
    /** Key to start a new game. */
    private String myNewControl;
    /** Key to quit the game. */
    private String myQuitControl;
   
    /** Constructor for the information panel. */
    public InformationPanel() {
        super();
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(Color.WHITE);
        setControls();
    }
    
    /**
     * Sets the default string representations of the game's controls. 
     */
    private void setControls() {
        myLeftControl = LEFT_ARROW;
        myRightControl = RIGHT_ARROW;
        myDownControl = DOWN_ARROW;
        myRotateControl = UP_ARROW;
        myDropControl = "space";
        myPauseControl = "p";
        myResumeControl = "r";
        myNewControl = "s";
        myQuitControl = "q";
    }
    
    /** 
     * Draws text representations of the game's controls.
     * 
     * @param theGraphics the Graphics object. 
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);      
        final Graphics2D g2d = (Graphics2D) theGraphics; 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        final int yOffset = 20;
        int y = START_Y_LOC;
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, TITLE_FONT_SIZE));
        g2d.drawString("c o n t r o l s", TEXT_X_LOC, y);
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));
        y += yOffset;
        g2d.drawString("left:  " + myLeftControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("right:  " + myRightControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("down:  " + myDownControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("rotate:  " + myRotateControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("drop:  " + myDropControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("pause:  " + myPauseControl, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("resume:  " + myResumeControl, TEXT_X_LOC, y);
        
        y += yOffset;
        g2d.drawString("end game:  " + myQuitControl, 2, y);
        y += yOffset;
        g2d.drawString("new game:   " + myNewControl, 2, y);
        
    }
    
    /**
     * Changes the information to reflect movement with reversed arrow keys for the
     * game's hard mode.
     */
    public void reverseMoveControls() {
        myLeftControl = RIGHT_ARROW;
        myRightControl = LEFT_ARROW;
        myDownControl = UP_ARROW;
        myRotateControl = DOWN_ARROW;
        repaint();
    }
    
    /**
     * Changes the information to reflect normal movement controls. 
     */
    public void restoreMoveControls() {
        myLeftControl = LEFT_ARROW;
        myRightControl = RIGHT_ARROW;
        myDownControl = DOWN_ARROW;
        myRotateControl = UP_ARROW;
        repaint();
    }

}
