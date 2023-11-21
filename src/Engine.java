import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.sound.sampled.*;

class BasePanel extends JPanel {

    private ArrayList<GameObject> gameObjects;

    BasePanel(int width, int height) {
        setSize(width, height);
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g);
        }
    }
}

class SoundPlayer {
    // private static URL jumpSound;
    // private static URL pointSound;
    // private static URL dieSound;
    // private static Clip clip;

    static void initialize() {
        // try {
        //     clip = AudioSystem.getClip();
        //     jumpSound = SoundPlayer.class.getResource(("assets/jump.wav"));
        //     pointSound = SoundPlayer.class.getResource(("assets/score.wav"));
        //     dieSound = SoundPlayer.class.getResource(("assets/die.wav"));
        //     System.out.println(jumpSound);

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    static void playSound(Callback.Type type) {

        // URL playUrl = null;
        // if (type == Callback.Type.JUMP) {
        //     playUrl = jumpSound;
        // } else if (type == Callback.Type.INCREMENT_SCORE) {
        //     playUrl = pointSound;
        // } else if (type == Callback.Type.GAME_ENDED) {
        //     playUrl = dieSound;
        // }

        // final URL finalUrl = playUrl;

        // if (playUrl != null) {
        //     new Thread(() -> {
        //         try {
        //             AudioInputStream stream = AudioSystem.getAudioInputStream(finalUrl);
        //             clip.setFramePosition(0);
        //             clip.close();
        //             clip.open(stream);
        //             Thread.sleep(1);
        //             clip.start();
        //             // clip.addLineListener((LineEvent e) -> {
        //             // System.out.println(e.getType());
        //             // if (e.getType() == LineEvent.Type.CLOSE) {
        //             // clip.close();
        //             // }
        //             // });
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //     }).start();
        // }
    }
}

public class Engine extends JFrame implements KeyListener {
    private static final int WINDOW_WIDTH = 576;
    private static final int WINDOW_HEIGHT = 512;
    // private static final int WINDOW_WIDTH = 1200;
    // private static final int WINDOW_HEIGHT = 800;
    private static final int UPDATE_PERIOD = 16;

    private boolean gameOver = false;

    ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    Timer updateTimer;
    Timer drawTimer;
    BasePanel basePanel;
    EngineCallback callbackHandler;

    public Engine(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);

        basePanel = new BasePanel(getWidth(), getHeight());
        basePanel.setGameObjects(gameObjects);
        add(basePanel);

        callbackHandler = new EngineCallback();
        SoundPlayer.initialize();
    }

    void add(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.onStart(callbackHandler);
    }

    void update() {
        for (GameObject gameObject : gameObjects) {
            gameObject.onUpdate();
        }
        detectCollisions();
    }

    void start() {
        updateTimer = new Timer(UPDATE_PERIOD, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        drawTimer = new Timer(UPDATE_PERIOD, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        for (GameObject gameObject : gameObjects) {
            gameObject.isEnabled = false;
        }

        drawTimer.start();
        updateTimer.start();
        System.out.println("Press SPACE to start, R to restart, ESC to pause");
        super.setVisible(true);
    }

    void detectCollisions() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject g1 = gameObjects.get(i);
            for (int j = i + 1; j < gameObjects.size(); j++) {
                GameObject g2 = gameObjects.get(j);
                isColliding(g1, g2);
            }
        }
    }

    private static void isColliding(GameObject g1, GameObject g2) {
        if (g1.children != null) {
            for (GameObject child : g1.children) {
                isColliding(child, g2);
            }
        } else if (g2.children != null) {
            for (GameObject child : g2.children) {
                isColliding(g1, child);
            }
        } else {
            if (g1.isRigid && g2.isRigid && g1.isEnabled && g2.isEnabled &&
                    g1.position.x < g2.position.x + g2.size.x &&
                    g1.position.x + g1.size.x > g2.position.x &&
                    g1.position.y < g2.position.y + g2.size.y &&
                    g1.position.y + g1.size.y > g2.position.y) {
                g1.onCollision(g2);
                g2.onCollision(g1);
            }
        }
    }

    private void reload() {
        gameOver = false;
        for (GameObject gameObject : gameObjects) {
            gameObject.onStart(callbackHandler);
        }
    }

    private GameObject findGameObjectByTag(String tag) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.tag == tag) {
                return gameObject;
            }
        }
        return null;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            reload();
            GameObject flashObject = findGameObjectByTag("FLASH");
            if (flashObject != null) {
                ((Flash) flashObject).startFlash(255);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                SoundPlayer.playSound(Callback.Type.JUMP);
                for (GameObject gameObject : gameObjects) {
                    gameObject.isEnabled = true;
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (updateTimer.isRunning()) {
                updateTimer.stop();
            } else {
                updateTimer.start();
            }
        }

        for (GameObject gameObject : gameObjects) {
            gameObject.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    class EngineCallback implements Callback {
        public void onCallback(Callback.Type type) {
            if (type == Callback.Type.GAME_ENDED) {
                SoundPlayer.playSound(Callback.Type.GAME_ENDED);
                gameOver = true;
                GameObject flashObject = findGameObjectByTag("FLASH");
                if (flashObject != null) {
                    ((Flash) flashObject).startFlash();
                }

                for (GameObject gameObject : gameObjects) {
                    gameObject.isAnimating = false;
                    if (gameObject.tag != "BIRD") {
                        gameObject.isEnabled = false;
                    }
                    if (gameObject.tag == "GAMEOVER") {
                        ((EndScreen) gameObject).gameOver();
                    }
                    gameObject.isRigid = false;
                }
            } else if (type == Callback.Type.INCREMENT_SCORE) {
                SoundPlayer.playSound(Callback.Type.INCREMENT_SCORE);
                GameObject scoreObject = findGameObjectByTag("SCORE");
                if (scoreObject != null) {
                    ((Score) scoreObject).incrementScore();
                }
            }
        }
    }
}
