import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment; 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class BasicFrame extends JFrame
{
        private Dimension screenDimension = 
                GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        private final double SCREEN_WIDTH_FRACTION = 1.0;
        private final double SCREEN_HEIGHT_FRACTION = 1.0;
        private final int WINDOW_WIDTH;
        private final int WINDOW_HEIGHT;

        public BasicFrame()
        {
                super("Wall Ball Wonder");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setLayout(new BorderLayout());
                WINDOW_WIDTH = (int) (screenDimension.width * SCREEN_WIDTH_FRACTION);
                WINDOW_HEIGHT = (int) (screenDimension.height * SCREEN_HEIGHT_FRACTION);
                setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                setVisible(true);
        }
}
