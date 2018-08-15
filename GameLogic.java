import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.*;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameLogic extends JPanel {

    // the state of the game logic
    //initialize objects

    public boolean playing = false; // whether the game is running
    private JLabel status; // Current status text (i.e. Running...)
    public Set<GameObject> objects = new TreeSet<GameObject>();

    // Game constants
    public static final int COURT_WIDTH = 200;
    public static final int COURT_HEIGHT = 300;
    public static final int DOODLE_VELOCITY = 2;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    public static final int NUM_PLATFORMS = 20;
    //create doodle
    public Doodle doodle = new Doodle();

    public GameLogic(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));


        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);

        // This key listener allows doodle to move right 
        //and left 
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    doodle.vx = -DOODLE_VELOCITY;
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    doodle.vx = DOODLE_VELOCITY;
            }

            public void keyReleased(KeyEvent e) {
                doodle.vx = 0;
            }
        });

        this.status = status;
    }
    
    public int getScore() {
        return doodle.getScore();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
       
      //show instructions pane
        JOptionPane.showMessageDialog(Game.frame, "Welcome to DoodleJump! \n Instructions:"
                + "Use the left and right arrow keys to move \n the doodle left and right on the "
                + "screen. \n Beware: red platforms are moving and blue platforms are broken "
                + "\n and will disappear if you try to bounce on them! \n Have fun!");
        
      //populate platforms
        for (int i = 0; i < NUM_PLATFORMS; i++) {
            double x = Math.random();
            if (x <= .1) {
                GameObject p = new BrokenPlatform();
                objects.add(p);
            } else if (x <= .9) {
                GameObject p = new Platform();
                objects.add(p);
            } else {
                GameObject p = new MovingPlatform();
                objects.add(p);
            }
        }
        
        doodle = new Doodle();
        
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // advance doodle
            doodle.move();

            Set<GameObject> toRemove = new TreeSet<GameObject>();

            // check for collisions
            boolean collisionOccurred = false;
            for (GameObject g : objects) {
                if (doodle.vy >= 0) {
                    g.vy = 0;
                }
                if (g.intersectBroken(doodle)) {
                   toRemove.add(g);
                } else if (doodle.collide(g)) {
                        collisionOccurred = true;
                }
                g.move();
            }
            
            //move platforms with doodle after collision
            if (collisionOccurred) {
                for (GameObject g : objects) {
                    if (doodle.y > COURT_HEIGHT / 5) {
                        g.vy = 5;
                    } else {
                        g.vy = 13;
                    }
                    g.move();
                    
                    //add platforms to remove below screen
                    if (g.y > GameLogic.COURT_HEIGHT) {
                        toRemove.add(g);
                    }

                }
                
                //add platforms to top of screen
                    for (int i = 0; i < NUM_PLATFORMS / 3; i++) {
                        double x = Math.random();
                        if (x <= .1) {
                            GameObject p = new BrokenPlatform();
                            p.y = COURT_HEIGHT / 10 - p.y;
                            objects.add(p);
                        } else if (x <= .9) {
                            GameObject p = new Platform();
                            p.y = COURT_HEIGHT / 10 - p.y;
                            objects.add(p);
                        } else {
                            GameObject p = new MovingPlatform();
                            p.y = COURT_HEIGHT / 10 - p.y;
                            objects.add(p);
                        }
                    }

                collisionOccurred = false;
            }
            
            //check if fall
            if (doodle.y > COURT_HEIGHT) {
                playing = false;
                //get high scores
                status.setText("Game Over!  Score: " + doodle.getScore());
                    try {
                        Reader in = new BufferedReader(new FileReader("scores.txt"));
                        JOptionPane pane = new JOptionPane();
                        Game.frame.add(pane);
                        String un = JOptionPane.showInputDialog("Please enter a username.");
                        int uScore = doodle.getScore();
                        HighScores score = new HighScores(in);
                        TreeMap<Integer, String> topFive = score.getHighScores();
                        topFive.put(uScore, un);
                        if (topFive.size() > 5) {
                            topFive.pollFirstEntry();
                        }
                        String[] arr = new String[5];
                        for (int i = 0; i < 5; i++) {
                            if (!topFive.isEmpty()) {
                                Map.Entry<Integer, String> m = topFive.pollLastEntry();
                                arr[i] = "" + m.getValue() + ", " + m.getKey();
                            } else {
                                arr[i] = "";
                            }
                        }
                        JOptionPane.showMessageDialog(null, "High Scores: \n" + arr[0] + "\n"+ arr[1] + 
                                "\n" + arr[2] + "\n" + arr[3] + "\n" + arr[4]);
                        Writer out = new BufferedWriter(new FileWriter("scores.txt"));
                        for (int i = 0; i < 5; i++) {
                            out.write(arr[i] + "\n");
                        }
                        in.close();
                        out.flush();
                        out.close();
                      } catch (IOException e) {
                        System.out.println("error while checking document: " + e.getMessage());
                      } catch (HighScores.FormatException e) {
                        System.out.println("format error while checking document: " + e.getMessage());
                      } finally {
                          System.exit(0);
                      }
            }
            
            // update the display
            objects.removeAll(toRemove);
            toRemove = new TreeSet<GameObject>();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doodle.draw(g);
        for (GameObject p : objects) {
            p.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public boolean getPlaying() {
        System.out.println("playing");
        return playing;
    }
}
