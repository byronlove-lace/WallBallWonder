import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class WallBallWonder 
{
        public static void main(String args[])
        {
                BasicFrame basicFrame = new BasicFrame();
                BallPane ballPane = new BallPane();

                basicFrame.add(ballPane);
                basicFrame.pack();

                ballPane.addBalls(5);

                ballPane.startBalls();
                basicFrame.setVisible(true);
        }
}
