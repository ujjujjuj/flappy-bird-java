import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class StartScreen extends GameObject {
    private final static int SCREEN_WIDTH = 576;
    private final static int SCREEN_HEIGHT = 512;
    private final static int IMG_WIDTH = 184;
    private final static int IMG_HEIGHT = 160;

    private final static int ALPHA_STEP = 3;
    private final static int MIN_ALPHA = 80;

    private int alpha = 255;
    boolean decrementing = true;
    boolean shouldErase = false;

    public StartScreen() {
        isRigid = false;
        position = new Vec2((SCREEN_WIDTH - IMG_WIDTH) / 2, (SCREEN_HEIGHT - IMG_HEIGHT - 100) / 2);
        setSprite("assets/start.png");
    }

    @Override
    public void onStart(Callback callback) {
        decrementing = true;
        alpha = 255;
        shouldErase = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shouldErase = true;
            decrementing = true;
        }
    }

    @Override
    public void onUpdate() {
        if (decrementing) {
            alpha -= ALPHA_STEP;
            if (!shouldErase && alpha < MIN_ALPHA) {
                alpha = MIN_ALPHA;
                decrementing = false;
            } else if (shouldErase) {
                if (alpha < 0) {
                    alpha = 0;
                }
            }
        } else {
            alpha += ALPHA_STEP;
            if (alpha > 255) {
                alpha = 255;
                decrementing = true;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (alpha == 0) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha / 255.f);
        g2d.setComposite(alphaComposite);
        g2d.drawImage(image, position.x, position.y, null);

        g2d.setComposite(oldComposite);

    }

}
