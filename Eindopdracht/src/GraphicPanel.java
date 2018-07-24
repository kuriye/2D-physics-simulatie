import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicPanel extends JPanel implements ActionListener, MouseListener {
    private Camera camera;
    private World world;
    private MousePicker mousePicker;
    private long lastTime;
    private ArrayList<PhysicsObject> physicObjects = new ArrayList<>();
    private BufferedImage background, debug, cursor, shape, openlock, closedlock, reset, triangle;

    private boolean state;
    private boolean showDebug;
    private boolean borderOn;

    private HeadPanel headpanel;
    private ObjectInfoPanel infoPanel;
    private TopPanel topPanel;

    private Body roof, leftBorder, rightBorder, currentBody;


    public GraphicPanel(){
        world = new World();
        world.setGravity(new Vector2(0,-9.89));

        state = false;
        showDebug = false;
        borderOn = true;

        imageGetter();
        floorMaker();
        borderMaker();

        headpanel = new HeadPanel(cursor, shape, debug, openlock , closedlock, reset, this, borderOn);
        infoPanel = new ObjectInfoPanel(this);
        topPanel = new TopPanel(this);

        lastTime = System.nanoTime();
        new Timer(15,this).start();
        camera = new Camera(this);
        mousePicker = new MousePicker(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        Rectangle2D.Double scherm = new Rectangle2D.Double(0,0,getWidth(), getHeight());

        //background
        g2d.setPaint(new TexturePaint(background, scherm));
        g2d.fill(scherm);

        AffineTransform originalTransform = g2d.getTransform();


        g2d.setTransform(camera.getTransform(getWidth(), getHeight()));
        g2d.scale(1,-1);



        for(PhysicsObject po : physicObjects)
        {
            po.draw(g2d);
        }

        //debug
        if(showDebug) {
            g2d.setColor(Color.yellow);
            DebugDraw.draw(g2d, world, 100);
        }

        g2d.setTransform(originalTransform);



        headpanel.paintComponent(g2d, showDebug, borderOn);
        infoPanel.paintComponent(g2d, currentBody);

        if(state) {
            topPanel.paintComponent(g2d, triangle);
        }
    }

    public void actionPerformed(ActionEvent e) {
        long time = System.nanoTime();
        double elapsedTime = (time - lastTime) / 1000000000.0;
        lastTime = time;

        try {
            if (mousePicker.getBody() != null)
                currentBody = mousePicker.getBody();

            mousePicker.update(world, camera.getTransform(getWidth(), getHeight()), 100);
        }
        catch(NullPointerException ex){
        }
        world.update(elapsedTime);

        repaint();
    }

    public void floorMaker() {
        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(40, 2));
        floor.getTransform().setTranslation(0, -5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        physicObjects.add(new PhysicsObject(floor, new Vector2(0, 0), 1));
    }

    public void borderMaker(){
       leftBorder = new Body();
        leftBorder.addFixture(Geometry.createRectangle(1 , 11));
        leftBorder.getTransform().setTranslation(-10.2,0);
        leftBorder.setMass(MassType.INFINITE);
        world.addBody(leftBorder);
        physicObjects.add(new PhysicsObject(leftBorder, new Vector2(0, 0 ), 1));

        rightBorder = new Body();
        rightBorder.addFixture(Geometry.createRectangle(1 , 11));
        rightBorder.getTransform().setTranslation(10.2,0);
        rightBorder.setMass(MassType.INFINITE);
        world.addBody(rightBorder);
        physicObjects.add(new PhysicsObject(rightBorder, new Vector2(0, 0 ), 1));

        roof = new Body();
        roof.addFixture(Geometry.createRectangle(20, 0.15));
        roof.getTransform().setTranslation(0, 5.25);
        roof.setMass(MassType.INFINITE);
        world.addBody(roof);
        physicObjects.add(new PhysicsObject(roof, new Vector2(0, 0), 1));
    }

    public void objectMaker(double width, double height, String image, double scale, int vorm){
        Body newBody = new Body();
        if(vorm == 0)
            newBody.addFixture(Geometry.createRectangle(width, height));
        else if(vorm == 1)
            newBody.addFixture(Geometry.createCircle(width/2));
        else if(vorm == 2)
            newBody.addFixture(Geometry.createIsoscelesTriangle(width,height));
        newBody.getTransform().setTranslation(0,2);
        newBody.getFixture(0).setRestitution(0.1);
        newBody.setMass(MassType.NORMAL);
        world.addBody(newBody);
        physicObjects.add(new PhysicsObject(image, newBody, new Vector2(0, 0), scale));
    }

    private void imageGetter(){
        try{
            background = ImageIO.read(getClass().getResource("/background.png"));
            shape = ImageIO.read(getClass().getResource("/shapes.png"));
            cursor = ImageIO.read(getClass().getResource("/cursor.png"));
            debug = ImageIO.read(getClass().getResource("/debug.png"));
            reset = ImageIO.read(getClass().getResource("/reset.png"));
            openlock = ImageIO.read(getClass().getResource("/openlock.png"));
            closedlock = ImageIO.read(getClass().getResource("/closedlock.png"));
            triangle = ImageIO.read(getClass().getResource("triangle.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if (e.getX() < 40 && e.getX() > 10 && e.getY() > 100 && e.getY() < 130) {
                state = false;
            }
            else if (e.getX() < 40 && e.getX() > 10 && e.getY() > 140 && e.getY() < 170) {
                state = true;
            }
            else if (e.getX() < 40 && e.getX() > 10 && e.getY() > 180 && e.getY() < 210) {
                if (showDebug == true)
                    showDebug = false;
                else
                    showDebug = true;

                state = false;
            }
            else if (e.getX() < 40 && e.getX() > 10 && e.getY() > 220 && e.getY() < 250) {
                world.removeAllBodies();
                physicObjects.clear();
                floorMaker();
                borderMaker();
                state = false;
                borderOn = true;
            }
            else if (e.getX() < 40 && e.getX() > 10 && e.getY() > 260 && e.getY() < 290) {
                if (borderOn == true) {
                    borderOn = false;
                    world.removeBody(roof);
                    world.removeBody(leftBorder);
                    world.removeBody(rightBorder);
                } else {
                    borderOn = true;
                    borderMaker();
                }
            }


            else if (e.getX() < 140 && e.getX() > 110 && e.getY() > 6 && e.getY() < 36){
                objectMaker(0.9, 0.9, "brickblock.png", 0.05, 0);
            }
            else if (e.getX() < 180 && e.getX() > 150 && e.getY() > 6 && e.getY() < 36){
                objectMaker(0.9, 0.9, "ball.png", 0.37, 1);
            }
            else if (e.getX() < 220 && e.getX() > 190 && e.getY() > 6 && e.getY() < 36){
                objectMaker(1.2, 0.8, "bill.png", 0.25, 2);
            }

            Point2D frictionPlus = new Point2D.Double(getWidth()-70 + 7.5,  145.5);
            Point2D frictionMin = new Point2D.Double(getWidth()-120 + 7.5,  145.5);
            Point2D ResititutionPlus = new Point2D.Double(getWidth()-70 + 7.5,  165.5);
            Point2D ResititutionMin = new Point2D.Double(getWidth()-120 + 7.5,  165.5);

//            double leftX = getWidth()-70 + 7.5;
//            double rightX = getWidth()-120 + 7.5;
//            double upY = 138 - 7.5;
//            double downY = 158 -7.5;
                    if (frictionPlus.distance(e.getX(), e.getY()) <= 7.5) {
                        currentBody.getFixture(0).setFriction(currentBody.getFixture(0).getFriction() - 0.1);
                    } else if (frictionMin.distance(e.getX(), e.getY()) <= 7.5) {
                        currentBody.getFixture(0).setFriction(currentBody.getFixture(0).getFriction() + 0.1);
                    } else if (ResititutionPlus.distance(e.getX(), e.getY()) <= 7.5) {
                        currentBody.getFixture(0).setRestitution(currentBody.getFixture(0).getRestitution() - 0.1);
                    } else if (ResititutionMin.distance(e.getX(), e.getY()) <= 7.5) {
                        currentBody.getFixture(0).setRestitution(currentBody.getFixture(0).getRestitution() + 0.1);
                    }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
