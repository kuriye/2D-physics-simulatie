import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HeadPanel {
    private ArrayList<BufferedImage> icons;
    private GraphicPanel panel;
    private String color;
    private boolean borderOn;

    public HeadPanel(BufferedImage cursor, BufferedImage shapes, BufferedImage debug, BufferedImage openlock, BufferedImage closedlock, BufferedImage reset, GraphicPanel panel, boolean borderOn){
        icons = new ArrayList<>();
        icons.add(cursor);
        icons.add(shapes);
        icons.add(debug);
        icons.add(openlock);
        icons.add(closedlock);
        icons.add(reset);

        this.borderOn = borderOn;
        this.panel = panel;
        color = "22A033";
    }

    public void paintComponent(Graphics g, boolean showDebug, boolean borderOn){
        Graphics2D g2d = (Graphics2D)g;

        //panel
        g2d.setColor(Color.lightGray);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.fill(new Rectangle2D.Double(0, 80, 50, 230));
        g2d.setColor(Color.darkGray);
        g2d.draw(new Rectangle2D.Double(0, 80, 50, 230));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //buttons
        for(int i= 0; i < 5; i++){
            if(showDebug && i == 2)
                g2d.setColor(Color.getHSBColor(0.27f, 1.0f, 0.8f));
            else if(borderOn && i == 4)
                g2d.setColor(Color.getHSBColor(0.27f, 1.0f, 0.8f));
            else if(i == 4){
                g2d.setColor(Color.red);
            }
            else
                g2d.setColor(Color.gray);
            g2d.fill(new Rectangle2D.Double(10, 100 + i * 40, 30, 30));
            g2d.setColor(Color.darkGray);
            g2d.draw(new Rectangle2D.Double(10, 100 + i * 40, 30, 30));
        }

        //icons
        AffineTransform tx = new AffineTransform();
        tx.translate(15,105);
        tx.scale(0.05,0.05);
        g2d.drawImage(icons.get(0), tx, null);

        tx.scale(20,20);
        tx.translate(0,40);
        tx.scale(0.05,0.05);
        g2d.drawImage(icons.get(1), tx, null );

        tx.scale(20,20);
        tx.translate(0,40);
        tx.scale(0.12,0.12);

        tx.scale(8.333333,8.333333);
        tx.translate(0,0);
        tx.scale(0.12,0.12);
        g2d.drawImage(icons.get(2), tx, null);

        tx.scale(8.33333, 8.33333);
        tx.translate(0,42);
        tx.scale(0.025, 0.025);
        g2d.drawImage(icons.get(5), tx , null);

        tx.scale(40,40);
        tx.translate(-1, 36);
        tx.scale(0.05,0.05);
        if(borderOn)
            g2d.drawImage(icons.get(4), tx, null);

        else
            g2d.drawImage(icons.get(3), tx, null);
    }
}
