import java.awt.Color;
import java.awt.Graphics;

public class Flash extends GameObject {
    private final static int SCREEN_WIDTH = 576;
    private final static int SCREEN_HEIGHT = 512;
    private final static int DECAY_SPEED = 10;
    private final static int START_VAL = 180;

    private int alpha = 0;

    public Flash() {
        isRigid = false;
        tag = "FLASH";
    }

    @Override
    public void onUpdate() {
        alpha -= DECAY_SPEED;
        alpha = Math.max(alpha, 0);
    }

    public void startFlash() {
        alpha = START_VAL;
    }

    public void startFlash(int startVal) {
        alpha = startVal;
    }

    @Override
    public void draw(Graphics g) {
        if (alpha == 0) {
            return;
        }
        Color flashColor = new Color(255, 255, 255, alpha);
        g.setColor(flashColor);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

}
