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

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * The GamePanel of the Tetris program. Shows a visual representation of the game's
 * current state.
 * @author James Roberts
 * @version 2 December 2016
 */
public class GamePanel extends JPanel implements Observer {
    /** Default Number of blocks that make up the board's width. */
    protected static final int DEFAULT_BOARD_WIDTH = 10;
    /** Default Number of blocks that make up the boards height. */
    protected static final int DEFAULT_BOARD_HEIGHT = 20;
    
    /** The Serial ID #. */
    private static final long serialVersionUID = -5737652951294745809L;
    /** Default Size of a block in pixels. */
    private static final int DEFAULT_BLOCK_SIZE = 30;
    /** The minimum size of a block in pixels. */
    private static final int MIN_BLOCK_SIZE = 15;
    
    /** Collection of blocks that visually represent the game's current state. */
    private final List<ColoredBlock> myBlocks;
    
    /** The Font Size. */
    private  int myFontSize;
    /** Number of blocks that make up the board's width. */
    private int myBoardWidth;
    /** Number of blocks that make up the boards height. */
    private int myBoardHeight;
    /** The size of a block in pixels. */
    private int myBlockSize;
    /** If there is currently a game being played. */
    private boolean myGameStatus;
    /** If the game is paused or running. */
    private boolean myRunningStatus;
    /** If the game's pieces are all one Color. */
    private boolean myBlockMonochrome;
    
    
    
    
    /** Constructor for the Game Panel. */
    public GamePanel() {
        super();
        myBlocks = new ArrayList<ColoredBlock>();
        myGameStatus = true;
        myRunningStatus = true;
        setDefaults();
        setGamePanelSize();
    }
    
    /**
     * Sets the board's width, height, block size and font size to defaults.
     */
    private void setDefaults() {
        myBoardWidth = DEFAULT_BOARD_WIDTH;
        myBoardHeight = DEFAULT_BOARD_HEIGHT;
        myBlockSize = DEFAULT_BLOCK_SIZE;
        myFontSize = DEFAULT_BOARD_WIDTH * 2;
        myBlockMonochrome = true;
    }
    
    /**
     * Sets the size of the panel based on the block size and the boards's
     * width and height. 
     */
    private void setGamePanelSize() {
        final int width = myBlockSize * myBoardWidth + 1;
        final int height = myBlockSize * myBoardHeight + 1;
        setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Paints a graphical representation of the game's current state. Covers
     * with a splash screen if paused and displays a message if the game is over. 
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);      
        final Graphics2D g2d = (Graphics2D) theGraphics; 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (myBlockMonochrome) {
            setBackground(Color.CYAN);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
        
        if (myRunningStatus) { //If game is not paused
            for (final ColoredBlock block : myBlocks) {
                g2d.setColor(block.getColor());
                g2d.fill(block.getShape());
                g2d.setColor(Color.BLACK);
                g2d.draw(block.getShape());
            }
        } else { //game paused, cover with splash screen
            final int xOffset = 5;
            g2d.fillRect(0, 0, myBoardWidth * myBlockSize, myBoardHeight * myBlockSize);
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, myFontSize));
            g2d.setColor(Color.WHITE);
            g2d.drawString("p a u s e d", xOffset, myBoardHeight / 2 * myBlockSize);
        }
        
        //Need to print game over message on top of finished game
        // if necessary
        if (!myGameStatus) {
            final int xOffset = 2;
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, myFontSize));
            g2d.drawString("g  a  m  e    o  v  e  r", xOffset, 
                           myBoardHeight / 2 * myBlockSize);
        }
        
    }
    
    /**
     * Sets if the game is paused or not.
     * @param thePauseStatus if the game is paused.
     */
    public void isPaused(final boolean thePauseStatus) {
        myRunningStatus = !thePauseStatus;
        repaint();
    }
    
    /**
     * Tells the GamePanel to display the game over message.
     * @param theGameStatus if the game is running.
     */
    public void gameOver(final boolean theGameStatus) {
        myGameStatus = !theGameStatus;
    }
    
    /**
     * Sets if the Game's pieces are displayed with a monochrome color scheme.
     * @param theChoice If the Pieces are monochrome or not. 
     */
    public void isMonochrome(final Boolean theChoice) {
        myBlockMonochrome = theChoice;
        
    }
    
    /**
     * If a string is passed updates the graphical representation of the board.
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObject instanceof String) {
            
            createBlocks((String) theObject);
            repaint();
        }
        
    }
    
    /**
     * Sets the Game Panel to display a game with the passed board width and height.
     * Only works if a game is not running. 
     * @param theWidth the width of the board.
     * @param theHeight the height of the board.
     */
    public void setGameSize(final int theWidth, final int theHeight) {
        if (!myGameStatus) {
            myBlocks.clear();
            myBoardWidth = theWidth;
            myBoardHeight = theHeight;
            if (theWidth < DEFAULT_BOARD_WIDTH) {
                myFontSize = theWidth * 2;
            } else {
                myFontSize = DEFAULT_BOARD_WIDTH * 2;
            }
            int size;
            if (theHeight > theWidth) {
                size = DEFAULT_BLOCK_SIZE + (DEFAULT_BOARD_HEIGHT - theHeight);
            
            } else {
                size = DEFAULT_BLOCK_SIZE + (DEFAULT_BOARD_WIDTH - theWidth);
            }
        
            if (size < MIN_BLOCK_SIZE) {
                size = MIN_BLOCK_SIZE;
            }
            myBlockSize = size;
            setGamePanelSize();
            repaint();
        }

    }
    
    /**
     * Creates a visual representation of the board's current state.
     * @param theBoard a string representation of the board.
     */
    private void createBlocks(final String theBoard) {
      //Find char in string rep where actual board starts.
        final int boardBeginning = (myBoardWidth + 3) * 4 + myBoardWidth + 2; 
        
        myBlocks.clear();
        int x = 0;
        int y = 0;
        int i = boardBeginning;
        while (theBoard.charAt(i) != '-') {
            if (theBoard.charAt(i) == ' ') {
                x++;
            } else if (theBoard.charAt(i) == '\n') {
                y++;
                x = 0;
            } else if (theBoard.charAt(i) != '|') {
                final Rectangle2D.Double rect = new Rectangle2D.Double(x * myBlockSize, 
                                                                       y * myBlockSize,
                                                                     myBlockSize, myBlockSize);
                final ColoredBlock block = new ColoredBlock(rect, theBoard.charAt(i));
                myBlocks.add(block);
                x++;
                
            }
            i++;
        }
        
    }
    
    /**
     * Representation of a single block of a Tetris piece.
     * @author James Roberts
     * @version 9 Dec. 2016
     *
     */
    private final class ColoredBlock {
        /** Rectangle representing the block. */
        private final Rectangle2D.Double myBlock;
        /** The color of the block. */
        private final Color myColor;
        
        /** 
         * Constructor for the ColoredBlock. 
         * @param theBlock shape representing the block. 
         * @param theSymbol Symbol that determins the block's color. 
         */
        ColoredBlock(final Rectangle2D.Double theBlock, final char theSymbol) {
            
            myBlock = theBlock;
            if (myBlockMonochrome) {
                myColor = Color.PINK;
            } else {
                myColor = findColor(theSymbol);
            }
        }
        
        /**
         * Find's the block's color based on the passed char.
         * @param theSymbol char corresponding to the block's color. 
         * @return Color of the Block. 
         */
        private Color findColor(final char theSymbol) {
            final Color color;
            
            if (theSymbol == 'I') {
                color = Color.CYAN;
            } else if (theSymbol == 'J') {
                color = Color.BLUE;
            } else if (theSymbol == 'L') {
                color = Color.ORANGE;
            } else if (theSymbol == 'O') {
                color = Color.YELLOW;
            } else if (theSymbol == 'S') {
                color = Color.GREEN.brighter();
                                
            } else if (theSymbol == 'T') {
                color = Color.MAGENTA;
            } else {
                color = Color.RED;
            }
            
            return color;
        }
        
        /**
         * Returns the Block's Rectangle.
         * @return the Rectangle representing the block. 
         */
        public Rectangle2D.Double getShape() {
            return myBlock;
        }
        /**
         * Returns the Block's color.
         * @return the block's color.
         */
        public Color getColor() {
            return myColor;
        }
    }

}
