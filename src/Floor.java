import java.awt.Graphics;
import java.util.ArrayList;

public class Floor extends GameObject {
    ArrayList<GameObject> fragments = new ArrayList<GameObject>();
    private static final int N_FRAGMENTS = 3;
    private static final int FRAGMENT_WIDTH = 336;

    @Override
    public void onStart(Callback callback) {
        isAnimating = true;
        position = new Vec2(0, 400);
        size = new Vec2(576, 112);
        isEnabled = true;
        isRigid = true;

        for (int i = 0; i < N_FRAGMENTS; i++) {
            GameObject frag = new GameObject();
            frag.setSprite("assets/floor.png");
            frag.position = new Vec2(i * FRAGMENT_WIDTH, position.y);
            fragments.add(frag);
        }
    }

    @Override
    public void onUpdate() {
        if (!isAnimating) {
            return;
        }
        for (GameObject frag : fragments) {
            frag.position.x -= Constants.SPEED;
        }
        if (fragments.get(0).position.x <= -FRAGMENT_WIDTH) {
            fragments.remove(0);

            int lastX = fragments.get(fragments.size() - 1).position.x;
            GameObject frag = new GameObject();
            frag.setSprite("assets/floor.png");
            frag.position = new Vec2(lastX + FRAGMENT_WIDTH, 400);
            fragments.add(frag);
        }
    }

    public void draw(Graphics g) {

        for (GameObject frag : fragments) {
            frag.draw(g);
        }
    }
}
