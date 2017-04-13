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
//import java.awt.List;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;


/**
 * A score panel for the Tetris game that calculates the score based on the
 * number of lines cleared. Displays the Score, the number of lines cleared,
 * the number of until the next level. and the current level.   
 * @author James Roberts
 * @version 10 Dec. 2016
 *
 */
public class ScorePanel extends JPanel implements Observer {
    /** The Serial ID #. */
    private static final long serialVersionUID = -5026253866240854235L;
    /** Width of the panel. */
    private static final int PANEL_WIDTH = 200;
    /** Height of the Panel. */
    private static final int PANEL_HEIGHT = 100;
    /** Number of lines in a level. */
    private static final int LINES_PER_LEVEL = 5;
    /** Value of clearing one line. */
    private static final int LINE_VALUE = 100;
    /** Font size for the Score. */
    private static final int TITLE_FONT_SIZE = 18;
    /** Size of the panel's font. */
    private static final int FONT_SIZE = 15;
    /** Font size for the "lines needed" statistic. */
    private static final int NEEDED_LINES_FONT_SIZE = 13;
    /** X location of the panel's text. */
    private static final int TEXT_X_LOC = 2;
    /** Y location of the panel's first line of text. */
    private static final int START_Y_LOC = 14;
    
    /** The current score. */
    private int myScore;
    /** The current level. */
    private int myLevel;
    /** The number of lines cleared. */
    private int myLines;
    /** Number of lines to clear before reaching the next level. */
    private int myNeededLines;
    /** If the next game is to be in hardmode or not. */
    private boolean myNextGameStyle;
    /** If the current game is in hardmode. */
    private boolean myGameStyle;
    
    /** 
     * Constructor for the ScorePanel. 
     */
    public ScorePanel() {
        super();
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.WHITE);
        myScore = 0;
        myLevel = 1;
        myLines = 0;
        myNeededLines = LINES_PER_LEVEL;
        myNextGameStyle = false;
        myGameStyle = false;
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);      
        final Graphics2D g2d = (Graphics2D) theGraphics; 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        final int yOffset = 20;
        int y = START_Y_LOC;
        
        if (myGameStyle) {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, TITLE_FONT_SIZE - 1));
            g2d.setColor(Color.RED);
            g2d.drawString("UR_HARDSCORE: " + myScore, TEXT_X_LOC, y);
        } else {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, TITLE_FONT_SIZE));
            g2d.drawString("SCORE: " + myScore, TEXT_X_LOC, y);
        }
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE));
        y += yOffset;
        g2d.drawString("Level: " + myLevel, TEXT_X_LOC, y);
        y += yOffset;
        g2d.drawString("Lines Cleared: " + myLines, TEXT_X_LOC, y);
        y += yOffset * 1.5; //Leave a larger space before very last line. 
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, NEEDED_LINES_FONT_SIZE));
        g2d.drawString(myNeededLines + " lines to next level", TEXT_X_LOC, y);
    }
    
    /**
     * If passed a collection of Integers representing Lines cleared, updates score state.
     * Updates include the score, lines cleared, lines to the next level and potentially 
     * the level.
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {

        if (theObject instanceof Integer[]) {
            updateScore((Integer[]) theObject);
            repaint();
        }
        
    }
    
    /**
     * Resets ScorePanel to it's initial state.
     */
    public void resetScore() {
        myScore = 0;
        myLines = 0;
        myLevel = 1;
        myNeededLines = LINES_PER_LEVEL;
        myGameStyle = myNextGameStyle;
        repaint();              
    }
    
    /**
     * Sets if the score panel is to represent a hard mode game after being reset. 
     * @param theChoice if the panel is to represent a hard game after being reset.
     */
    public void nextGameHardmode(final boolean theChoice) {
        myNextGameStyle = theChoice;
    }
    
    /**
     * Updates the score, lines field and the needed lines field.
     * Each line cleared is worth 100 * the current level. 
     * @param theLines Collection of Lines that were cleared. 
     */
    private void updateScore(final Integer[] theLines) {
        int multiplier = 1;
        if (myGameStyle) { //game is hardmode
            multiplier = 2;
        }
        
        for (int i = 0; i < theLines.length; i++)  {
            myLines++;
            myNeededLines--;
            myScore += myLevel * LINE_VALUE * multiplier;
            
            if (myNeededLines == 0) { //Reached a new level
                myLevel++;
                myNeededLines = LINES_PER_LEVEL;
                firePropertyChange("LevelUP", null, (Integer) myLevel);
            }
            
        }
    }
    

}
