import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class EndScreen extends GameObject {
    private final static int SCREEN_WIDTH = 576;
    private final static int SCREEN_HEIGHT = 512;
    private final static int IMG_WIDTH = 192;
    private final static int IMG_HEIGHT = 42;

    private final static int ALPHA_STEP = 5;

    private int alpha = 0;
    private boolean isOver = false;

    public EndScreen() {
        isRigid = false;
        position = new Vec2((SCREEN_WIDTH - IMG_WIDTH) / 2, (SCREEN_HEIGHT - IMG_HEIGHT - 100) / 2);
        setSprite("assets/gameover.png");
        tag="GAMEOVER";
    }

    @Override
    public void onStart(Callback callback) {
        alpha = 0;
        isOver = false;
    }

    public void gameOver() {
        isOver = true;
    }

    @Override
    public void onUpdate() {
        if (isOver) {
            alpha += ALPHA_STEP;
            if (alpha > 255) {
                alpha = 255;
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
