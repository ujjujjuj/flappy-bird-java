import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameObject {
    public Vec2 position = new Vec2(0, 0);
    public Vec2 size = new Vec2(0, 0);
    public float rotation = 0;
    BufferedImage image;
    ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
    public boolean isEnabled = true;
    public boolean isRigid = true;
    public boolean isTrigger = false;
    public boolean isAnimating = false;
    public int spriteDuration = 6;
    private int timeElapsed = 0;

    ArrayList<GameObject> children;
    String tag = "";
    private int currentSpriteIndex = 0;

    public void onStart(Callback callback) {
    }

    public void onUpdate() {
    }

    public void onCollision(GameObject other) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void setSprite(String path) {
        try {
            image = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSprites(String[] paths) {
        for (String path : paths) {
            try {
                image = ImageIO.read(this.getClass().getClassLoader().getResource(path));
                images.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Graphics g) {
        BufferedImage img = image;
        if (images.size() > 0 && isAnimating) {
            img = images.get(currentSpriteIndex);
            timeElapsed++;
            if (timeElapsed > spriteDuration) {
                timeElapsed = 0;
                currentSpriteIndex = (currentSpriteIndex + 1) % images.size();
            }
        }

        if (img != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();
            g2d.translate(position.x + size.x / 2, position.y + size.y / 2);
            g2d.rotate(Math.toRadians(rotation));
            g2d.translate(-position.x - size.x / 2, -position.y - size.y / 2);
            g2d.drawImage(img, position.x, position.y, null);
            g2d.setTransform(old);
        }

    }
}
