import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Score extends GameObject {
    private final static int DIGIT_WIDTH = 24;
    private final static int DIGIT_GAP = 2;
    private final static int SCORE_Y = 20;

    private final static int ALPHA_RATE = 3;
    
    GameObject[] digits = new GameObject[10];

    private int score = 0;
    private int alpha = 0;

    public Score() {
        isRigid = false;
        for (int i = 0; i < 10; i++) {
            GameObject gameObject = new GameObject();
            gameObject.position = new Vec2(276, SCORE_Y);
            gameObject.size = new Vec2(24, 36);
            gameObject.isRigid = false;
            gameObject.setSprite("assets/" + i + ".png");
            digits[i] = gameObject;
        }
        tag = "SCORE";
    }

    @Override
    public void onStart(Callback callback) {
        score = 0;
        alpha = 0;
        isEnabled = false;
    }

    @Override
    public void onUpdate() {
        if (isEnabled) {
            alpha += ALPHA_RATE;
            if(alpha > 255){
                alpha = 255;
            }
        }
    }

    public void incrementScore() {
        score++;
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

        String scoreStr = String.valueOf(score);
        int startX = (576 - (scoreStr.length() - 1) * (DIGIT_WIDTH + DIGIT_GAP) - DIGIT_WIDTH) / 2;
        for (int i = 0; i < scoreStr.length(); i++) {
            int digit = scoreStr.charAt(i) - '0';
            digits[digit].position.x = startX + i * (DIGIT_WIDTH + DIGIT_GAP);
            digits[digit].draw(g);
            if (digit == 1) {
                startX -= 8;
            }
        }

        g2d.setComposite(oldComposite);
    }

}
