/*
* TCSS 305 – Autumn 2016
* Assignment 6 – Tetris
*/
package view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;

import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.Board;

/**
 * The GUI for the Tetris Program.
 * @author James Roberts
 * @version 2 Dec 2016
 *
 */
public final class TetrisGUI implements Observer, PropertyChangeListener {

    /** How often the game moves a piece down in m/s. */
    private static final int MY_UPDATE_TIME = 850;
    /** Max update speed the game can reach, in m/s. */
    private static final int MAX_SPEED = 100;
    /** Factor to increase the speed by for each level. */
    private static final int DELAY_UPDATE_FACTOR = 25;
    
    /** The frame of the GUI. */
    private final JFrame myTetrisFrame;
    /** The game's timer. */
    private final Timer myTimer;
    /** Visual representation of the game's board. */
    private final GamePanel myGamePanel;
    /** Visual representation of the game's score. */
    private final ScorePanel myScorePanel;
    /** Visual representation of the game's next piece. */
    private final NextPiecePanel myNextPiecePanel;
    /** The game control info panel. */
    private final InformationPanel myInfoPanel;
    /** The game's logical board. */
    private Board myBoard;
    /** If the game is running. */
    private boolean myGameRunStatus;
    /** Width of the next game. */
    private int myNextBoardWidth;
    /** Height of the next game. */
    private int myNextBoardHeight;
    /** If the current game is in hard mode. */
    private boolean myHardStatus;
    /** If the next game is to be in hard mode. */
    private boolean myNextGameStatus;
    
    
    /** Constructor for the game's GUI. */
    public TetrisGUI() {
        myTetrisFrame = new JFrame("TCSS 305 - Tetris");
        myBoard = new Board();
        myGamePanel = new GamePanel();
        myNextPiecePanel = new NextPiecePanel();
        myScorePanel = new ScorePanel();
        myInfoPanel = new InformationPanel();
        myTimer = createTimer();
        setDefaultState();
        
    }
    
    /**
     * Sets the game's options to the default values that reflect
     * the menu selection. 
     */
    private void setDefaultState() {
        myGameRunStatus = true;
        myNextBoardWidth = myBoard.getWidth();
        myNextBoardHeight = myBoard.getHeight();
        myHardStatus = false;
        myNextGameStatus = false;
    }
    
    /**
     * Creates the game's Timer.
     * @return A Timer for the game. 
     */
    private Timer createTimer() {
        return new Timer(MY_UPDATE_TIME, new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myBoard.down();
            }
        });
    }
    
    /**
     * Creates and adds each component of the GUI and sets the frame's operations.
     */
    public void start() {
       
        
        myTetrisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add observers, property change listeners and create menu
        addBoardObservers();
        final TetrisMenu menu = new TetrisMenu(myTimer, myGamePanel);
        myTetrisFrame.setJMenuBar(menu);
        menu.addPropertyChangeListener(this);
        
        
        //Set the layout and add the components 
        myTetrisFrame.setLayout(new FlowLayout());
        myTetrisFrame.add(myGamePanel);
        myScorePanel.addPropertyChangeListener(this);
        
        final JPanel rightPanel = new JPanel();
        final BoxLayout box = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        final int spacingHeight = 30; //number of pixels to separate 
                                      //the info box, next piece box and score
        rightPanel.setLayout(box);
        rightPanel.add(myNextPiecePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, spacingHeight)));
        rightPanel.add(myScorePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, spacingHeight)));
        rightPanel.add(myInfoPanel);
        myTetrisFrame.add(rightPanel);

        //Create key actions and start a new game
        createPieceMovementActions();
        createPieceControlActions();
        createPauseActions();
        createGameControlActions();
        myBoard.newGame();
        myTimer.start();
        
        //Set final properties of the JFrame
        myTetrisFrame.pack();
        myTetrisFrame.setSize(myTetrisFrame.getSize());
        myTetrisFrame.setResizable(false);
        myTetrisFrame.setLocationRelativeTo(null);
        myTetrisFrame.setVisible(true);
        
    }
    
    /**
     * Attaches all of the Board's Observers.
     */
    private void addBoardObservers() {
        myBoard.addObserver(this);
        myBoard.addObserver(myGamePanel);
        myBoard.addObserver(myNextPiecePanel);
        myBoard.addObserver(myScorePanel);
        
    }
    
    /** Creates the keyActions for a piece's movement in the game. */
    private void createPieceMovementActions() {
        String actionName = "leftAction";
        
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** Serial ID #. */
            private static final long serialVersionUID = 2147959870417233369L;
            /**  
             * Attempts the move the current piece left.
             * @param the left key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myTimer.isRunning()) {
                    myBoard.left();
                }
            }
        });
        
        actionName = "rightAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            
            /** Serial ID #. */
            private static final long serialVersionUID = -2520335842212904459L;
            /**  
             * Attempts the move the current piece right.
             * @param the right key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myTimer.isRunning()) {
                    myBoard.right();
                }
            }
        });
        
        actionName = "downAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** Serial ID #. */
            private static final long serialVersionUID = -6061782707376461597L;
            /**  
             * Attempts the move the current piece down.
             * @param the down key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myTimer.isRunning()) {
                    myBoard.down();
                }
            }
        });
    }
    
    
    /**
     * Creates the keyActions for the game that control the falling piece.
     */   
    private void createPieceControlActions() {
        String actionName = "upAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** Serial ID #. */
            private static final long serialVersionUID = 8429321738785310841L;
            /**  
             * Rotates the current piece.
             * @param the up key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myTimer.isRunning()) {
                    myBoard.rotate();
                }
            }
        });
        actionName = "spaceAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** The Serial ID #. */
            private static final long serialVersionUID = -7821559788573487718L;
            /**  
             * Drops the current piece. 
             * @param the space key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myTimer.isRunning()) {
                    myBoard.drop();
                }
            }
        });
    }
    
    
    /** Creates the pause and resume buttons. */
    private void createPauseActions() {
        String actionName = "pAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke('p'), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** Serial ID #. */
            private static final long serialVersionUID = -1170335097964398005L;
            /**  
             * Pauses the game. 
             * @param the p key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myGameRunStatus && myTimer.isRunning()) {
                    myTimer.stop();
                    myGamePanel.isPaused(true);
                }
            }
        });
        
        actionName = "resumeAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke('r'), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** The Serial ID #. */
            private static final long serialVersionUID = -7702759129016458196L;
            /**  
             * Resumes the game if it is paused.
             * @param the r key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myGameRunStatus && !myTimer.isRunning()) {
                    myTimer.start();
                    myGamePanel.isPaused(false);
                }
            }
        });
    }
    
    /** Creates the keyActions for the game that control the game state. */
    private void createGameControlActions() {
        String actionName = "endAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke('q'), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {
            /** The Serial ID #. */
            private static final long serialVersionUID = -1174172711197665189L;


            /**  
             * Ends the game if it is running.
             * @param the q key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (myGameRunStatus) {
                    endGame();
                }
            }
        });
        
        actionName = "startAction";
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke('s'), actionName);
        myGamePanel.getActionMap().put(actionName, new AbstractAction() {

            /** The Serial ID #. */
            private static final long serialVersionUID = -4095881425131474534L;

            /**  
             * Starts a game if no game is running.
             * @param the s key being pressed. 
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (!myGameRunStatus) {
                    restartGame();
                }
            }
        });
    }
    
    /**
     * Reverses the control keys for the game so the left arrow moves a piece right,
     * the right arrow moves a piece left, the up arrow moves a piece down and the down
     * arrow rotates a piece.
     */
    private void reversePieceControls() {
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "rightAction");
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "leftAction");        
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "downAction");        
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "upAction");
  
    }
    
    /**
     * Restores the up, down, right and left arrow key controls to their initial state. 
     */
    private void restorePieceControls() {
       
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftAction");      
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upAction");
        myGamePanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downAction");
    }
    
    /**
     * If game is running ends the game.
     */
    private void endGame() {
        myTimer.stop();
        myGamePanel.isPaused(false);
        myGamePanel.gameOver(true);
        myGameRunStatus = false;
    }
    
    /**
     * If no game is running, starts a new game. 
     */
    private void restartGame() {
        //make new board, reset everything and apply user selected options. 
        myBoard = new Board(myNextBoardWidth, myNextBoardHeight);
        myBoard.newGame();
        myScorePanel.resetScore();
        myGamePanel.setGameSize(myNextBoardWidth, myNextBoardHeight);
        addBoardObservers();
        myHardStatus = myNextGameStatus;
        myGamePanel.gameOver(false);
        myGameRunStatus = true;
        //Resize frame if needed
        myTetrisFrame.pack();
        myTetrisFrame.setSize(myTetrisFrame.getSize());
        myTetrisFrame.setResizable(false);
        myTimer.start();
    }
    
    /**
     * If passed a Boolean updates if the game is over.
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        
        if (theObject instanceof Boolean && ((Boolean) theObject).booleanValue()) {
            myTimer.stop();
            myGamePanel.gameOver(true);
            myGameRunStatus = false;
        }
        
    }
    
    /**
     * Performs a variety of operations depending on the Event. Speeds up the timer
     * after each level, handles the game's width, height and mode being changed in the
     * menu.
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("LevelUP".equals(theEvent.getPropertyName())) { //ScorePanel reached a new level
            int newDelay = myTimer.getDelay() 
                            - ((int) theEvent.getNewValue() - 1) * DELAY_UPDATE_FACTOR;
            if (newDelay < MAX_SPEED) {
                newDelay = MAX_SPEED;
            }
            myTimer.setDelay(newDelay);
            
            if (myHardStatus) { //Hard Mode game
                if ((int) theEvent.getNewValue() % 2 == 0) { //even level, controls switched.
                    reversePieceControls();
                    myInfoPanel.reverseMoveControls();
                } else {
                    restorePieceControls();
                    myInfoPanel.restoreMoveControls();
                }
            }

        }
        
        if ("HardMode".equals(theEvent.getPropertyName())) { //From HardMode check box
            myScorePanel.nextGameHardmode((boolean) theEvent.getNewValue());
            myNextGameStatus = (boolean) theEvent.getNewValue();
        }
        
        if ("WidthSetting".equals(theEvent.getPropertyName())) { //Menu width slider
            myNextBoardWidth = (int) theEvent.getNewValue();
        }
        
        if ("HeightSetting".equals(theEvent.getPropertyName())) { //Menu height slider
            myNextBoardHeight = (int) theEvent.getNewValue();
        }
        
    }
}
    

