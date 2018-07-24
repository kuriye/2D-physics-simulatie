import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MousePicker {
    Point mousePos;

    Body body;
    MotorJoint joint;

    public MousePicker(JPanel panel)
    {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(SwingUtilities.isLeftMouseButton(e))
                    mousePos = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                mousePos = null;
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(SwingUtilities.isLeftMouseButton(e))
                    mousePos = e.getPoint();
            }
        });
    }

    public Body getBody() {
        return body;
    }

    void update(World world, AffineTransform transform, double scale)
    {
        if(mousePos != null)
        {
            try {
                Point2D localMouse = transform.inverseTransform(mousePos, null);
                localMouse = new Point2D.Double(localMouse.getX()/scale, localMouse.getY()/-scale);

                if(body == null && joint == null) {
                    Convex convex = Geometry.createCircle(0.1);
                    Transform tx = new Transform();
                    tx.translate(localMouse.getX(), localMouse.getY());

                    // detect bodies under the mouse pointer
                    List<DetectResult> results = new ArrayList<>();

                    boolean detect = world.detect(
                            convex,
                            tx,
                            null,      // no, don't filter anything using the Filters
                            false,      // include sensor fixtures
                            false,      // include inactive bodies
                            false,      // we don't need collision info
                            results);

                    if (detect) {
                        Body target = results.get(0).getBody();

                        target.setAutoSleepingEnabled(false);
                        target.setAsleep(false);
                        body = new Body();
                        body.setMass(MassType.INFINITE);
                        body.addFixture(convex);
                        body.getTransform().setTranslation(localMouse.getX(), localMouse.getY());
                        world.addBody(body);

                        joint = new MotorJoint(target, body);
                        joint.setCollisionAllowed(false);
                        joint.setMaximumForce(1000.0);
                        joint.setMaximumTorque(0.01);

                        world.addJoint(joint);


                    }
                }

                if(body != null)
                {
                    body.getTransform().setTranslation(localMouse.getX(), localMouse.getY());
                }



            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }


        }
        else // niet op muis gedrukt
        {
            if(body != null)
            {
                world.removeBody(body);
                world.removeJoint(joint);
                body = null;
                joint = null;
            }
        }
    }
}
