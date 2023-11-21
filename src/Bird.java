import java.awt.event.KeyEvent;

public class Bird extends GameObject {
    private static final float GRAVITY = 0.27f;
    private static final int START_X = 80;
    private static final int START_Y = 200;
    private static final int JUMP_SPEED = 5;

    private float speedY;
    private float accelY = GRAVITY;

    Callback callback;

    Bird() {
        setSprites(new String[] { "assets/bird_mid.png", "assets/bird_down.png", "assets/bird_up.png" });
        tag = "BIRD";
    }

    @Override
    public void onStart(Callback callback) {
        position = new Vec2(START_X, START_Y);
        size = new Vec2(34, 24);
        speedY = 0;
        rotation = 0;
        isEnabled = false;
        isRigid = true;
        isAnimating = true;

        this.callback = callback;
    }

    @Override
    public void onCollision(GameObject other) {
        if (other.isTrigger) {
            other.isEnabled = false;
            callback.onCallback(Callback.Type.INCREMENT_SCORE);
        } else {
            speedY = 3;
            callback.onCallback(Callback.Type.GAME_ENDED);
        }
    }

    @Override
    public void onUpdate() {
        if (!isEnabled) {
            return;
        }
        speedY -= accelY;
        position.y -= Math.round(speedY);
        if (position.y < 0) {
            position.y = 0;
            speedY = 0;
        } else if (position.y >= 400 - size.y) {
            position.y = 400 - size.y + 1;
            return;
        }
        if (speedY >= 0) {
            rotation = Math.max(-18, rotation - 12);
        } else {
            rotation = Math.min(90, rotation + 3);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isAnimating) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            speedY = JUMP_SPEED;
        }
    }
}
