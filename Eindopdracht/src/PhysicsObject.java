import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PhysicsObject {
    Body body;
    BufferedImage image;
    Vector2 offset;
    double scale;

    PhysicsObject(String imageFile, Body body, Vector2 offset, double scale)
    {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        try {
            image = ImageIO.read(getClass().getResource(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PhysicsObject(Body body, Vector2 offset, double scale){
        this.body = body;
        this.offset = offset;
        this.scale = scale;
    }

    PhysicsObject(BufferedImage image, Body body, Vector2 offset, double scale)
    {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        this.image = image;
    }

    public void draw(Graphics2D g2d)
    {
        if(image == null)
            return;

        AffineTransform tx = new AffineTransform();
        tx.translate(body.getTransform().getTranslationX() * 100, body.getTransform().getTranslationY() * 100);
        tx.rotate(body.getTransform().getRotation());
        tx.scale(scale, -scale);
        tx.translate(offset.x, offset.y);

        tx.translate(-image.getWidth()/2, -image.getHeight()/2);
        g2d.drawImage(image, tx, null);
    }
}
