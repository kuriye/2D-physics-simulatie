import javax.swing.*;
import java.awt.*;

public class Main  extends JFrame{

    public static void main(String[] args){
        Main main = new Main();
    }

    public Main(){
        super("Physics are fun");
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1800, 900));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(new GraphicPanel());
        setVisible(true);
    }
}
