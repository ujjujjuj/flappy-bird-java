import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class PipeManager extends GameObject {

    private static final int PIPE_WIDTH = 52;
    private static final int PIPE_HEIGHT = 320;

    private static final int COLUMNS = 5;
    // private static final int START_X = 400;
    private static final int START_X = 600;
    private static final int GAP_X = 90;
    private static final int GAP_Y = 110;
    private static final int Y_PAD = 30;

    private static Random rng = new Random(System.currentTimeMillis());

    public PipeManager() {
        setSprite("assets/bg.png");
        isRigid = false;
    }

    @Override
    public void onStart(Callback callback) {
        children = new ArrayList<GameObject>();
        position = new Vec2(0, 0);
        size = new Vec2(576, 400);
        isEnabled = false;
        isRigid = true;

        for (int i = 0; i < COLUMNS; i++) {
            GameObject[] column = createColumn(START_X + i * (GAP_X + PIPE_WIDTH));
            children.add(column[0]);
            children.add(column[1]);
            children.add(column[2]);
        }
    }

    public GameObject[] createColumn(int x) {
        int topY = rng.nextInt(size.y - GAP_Y + 1 - 2 * Y_PAD) + Y_PAD - PIPE_HEIGHT;

        GameObject topPipe = new GameObject();
        GameObject bottomPipe = new GameObject();
        topPipe.setSprite("assets/pipe_top.png");
        bottomPipe.setSprite("assets/pipe_bottom.png");
        topPipe.size = new Vec2(PIPE_WIDTH, PIPE_HEIGHT);
        bottomPipe.size = new Vec2(PIPE_WIDTH, PIPE_HEIGHT);
        topPipe.position = new Vec2(x, topY);
        bottomPipe.position = new Vec2(x, topY + PIPE_HEIGHT + GAP_Y);

        GameObject scoreBox = new GameObject();
        scoreBox.size = new Vec2(1, GAP_Y);
        scoreBox.position = new Vec2(x + GAP_X / 2, topY + PIPE_HEIGHT);
        scoreBox.isTrigger = true;

        return new GameObject[] { topPipe, bottomPipe, scoreBox };

    }

    @Override
    public void onUpdate() {
        if (!isEnabled) {
            return;
        }
        for (GameObject pipe : children) {
            pipe.position.x -= Constants.SPEED;
        }

        if (children.get(0).position.x <= -PIPE_WIDTH) {
            children.remove(0);
            children.remove(0);
            children.remove(0);

            int lastX = children.get(children.size() - 1).position.x;
            GameObject[] column = createColumn(lastX + GAP_X + PIPE_WIDTH);
            children.add(column[0]);
            children.add(column[1]);
            children.add(column[2]);
        }
    }

    public void draw(Graphics g) {
        super.draw(g);
        for (GameObject pipe : children) {
            pipe.draw(g);
        }
    }
}
