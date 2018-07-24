import org.dyn4j.geometry.Triangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TopPanel {
    GraphicPanel panel;

    public TopPanel(GraphicPanel panel) {
        this.panel = panel;
    }

    public void paintComponent(Graphics g, BufferedImage triangle) {
        Graphics2D g2d = (Graphics2D) g;

        //panel
        g2d.setColor(Color.lightGray);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.fill(new Rectangle2D.Double(100, -4, 130, 50));
        g2d.setColor(Color.darkGray);
        g2d.draw(new Rectangle2D.Double(100, -4, 130, 50));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //buttons
        for(int i= 0; i <3; i++){
            g2d.setColor(Color.gray);
            g2d.fill(new Rectangle2D.Double(110 + i * 40,6 , 30, 30));
            g2d.setColor(Color.darkGray);
            g2d.draw(new Rectangle2D.Double(110 + i * 40,6 , 30, 30));
        }

        //icons
        AffineTransform tx = new AffineTransform();
        g2d.setColor(Color.black);
        g2d.fill(new Rectangle2D.Double(116, 12, 20,20));
        g2d.fill(new Ellipse2D.Double(155, 12,  20 ,20));
        tx.translate(193, 10);
        tx.scale(0.05, 0.05);
        g2d.drawImage(triangle, tx, null);
    }
}
