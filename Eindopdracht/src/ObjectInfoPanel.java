import org.dyn4j.dynamics.Body;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ObjectInfoPanel  {
    private GraphicPanel panel;

    public ObjectInfoPanel(GraphicPanel panel) {
        this.panel = panel;
    }

    public void paintComponent(Graphics g, Body body) {
        Graphics2D g2d = (Graphics2D) g;

        if(body != null) {
            g2d.setColor(Color.lightGray);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.fill(new RoundRectangle2D.Double(panel.getWidth() - 210, 80, 200, 100, 20, 15));
            g2d.setColor(Color.darkGray);
            g2d.draw(new RoundRectangle2D.Double(panel.getWidth() - 210, 80, 200, 100, 20, 15));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g2d.setColor(Color.getHSBColor(0.27f, 1.0f, 0.8f));
            g2d.fillOval(panel.getWidth() - 120, 138, 15 ,15);
            g2d.fillOval(panel.getWidth() - 120, 158, 15 ,15);

            g2d.setColor(Color.red);
            g2d.fillOval(panel.getWidth() - 70, 138, 15 ,15);
            g2d.fillOval(panel.getWidth() - 70, 158, 15 ,15);

            g2d.setColor(Color.black);
            g2d.drawOval(panel.getWidth() - 70, 138, 15 ,15);
            g2d.drawOval(panel.getWidth() - 120, 138, 15 ,15);
            g2d.drawOval(panel.getWidth() - 70, 158, 15 ,15);
            g2d.drawOval(panel.getWidth() - 120, 158, 15 ,15);

            g2d.drawString("Object information:        ", panel.getWidth() - 200, 105);
            //g2d.drawString("shape:        ", panel.getWidth() - 200, 130);
            //if(body.getFixture(0).getShape().toString())
            g2d.drawString("Friction:              +    " + body.getFixture(0).getFriction() + "     -", panel.getWidth() - 200, 150);
            // System.out.println(body.getFixture(0).getShape().toString());
            g2d.drawString("Restitution:        +    " + body.getFixture(0).getRestitution() + "     -", panel.getWidth() - 200, 170);
        }
    }
}
