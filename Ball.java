import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment; 
import java.awt.geom.Ellipse2D;
import java.security.SecureRandom;
import javax.swing.JComponent;

class Ball implements Runnable
{
        private Dimension windowSize;
        private final Color color;
        private final double BALL_DIAMETER_FRACTION = 0.05;
        private final int FRAME_MOVEMENT = 2;
        private final int ANIMATION_CYCLE = 25;
        private int windowWidth;
        private int windowLength;
        private boolean moveUp, moveLeft;
        private Ellipse2D circle;
        // top left corner of square containing circle
        public double posX;
        public double posY;
        public double centX;
        public double centY;
        public final double ballDiameter;

        public Ball(Color color, int windowWidth, int windowLength)
        {
                this.windowWidth = windowWidth;
                this.windowLength = windowLength;
                this.color = color;
                this.ballDiameter = BALL_DIAMETER_FRACTION * windowWidth;
                this.posX = windowWidth * randomStartFraction();
                this.posY = windowLength * randomStartFraction();
                circle = new Ellipse2D.Double(posX, posY, ballDiameter, ballDiameter);
                this.moveUp = randomStartDirection();
                this.moveLeft = randomStartDirection();
        }

        // Getters
        public double getBallDiameter() {
                return ballDiameter;
        }

        // Methods
        private double randomStartFraction()
        {
                SecureRandom randomNum = new SecureRandom();
                double randomFraction;
                double min = 0.00 - BALL_DIAMETER_FRACTION;
                double max = 1.00 - BALL_DIAMETER_FRACTION;

                randomFraction = randomNum.nextDouble() * (max - min) + min;

                return randomFraction;
        }

        private boolean randomStartDirection()
        {
                SecureRandom randomNum = new SecureRandom();
                int direction = randomNum.nextInt(2);

                if (direction == 1)
                        return true;
                else
                        return false;
        }

        public void getCenter(){
                centX = posX + (ballDiameter / 2);
                centY = posY + (ballDiameter / 2);
        }

        public void bounceLeft(){
                moveLeft = true;
        }

        public void bounceRight(){
                moveLeft = false;
        }

        public void bounceUp(){
                moveUp = true;
        }

        public void bounceDown(){
                moveUp = false;
        }

        public void paint(Graphics2D g2d)
        {
                g2d.setColor(color);
                g2d.fill(circle);
        }

        // Overrides
        @Override
        public void run()
        {
                while (true)
                {
                        if (posY >= (windowLength - ballDiameter))
                        {
                                moveUp = true;
                                posY = windowLength - ballDiameter;
                        }

                        if (posY < 0)
                        {
                                moveUp = false;
                        }

                        if (posX > (windowWidth - ballDiameter))
                        {
                                moveLeft = true;
                        }

                        if (posX < 0)
                        {
                                moveLeft = false;
                        }

                        BallPane.lock.lock();
                        if (moveUp)
                        {
                                posY -= FRAME_MOVEMENT;
                        }
                        else
                        {
                                posY += FRAME_MOVEMENT;
                        }

                        if (moveLeft)
                        {
                                posX -= FRAME_MOVEMENT;
                        }
                        else
                        {
                                posX += FRAME_MOVEMENT;
                        }

                        circle.setFrame(posX, posY, ballDiameter, ballDiameter);
                        ++BallPane.completedCycles;

                        BallPane.lock.unlock();

                        try 
                        {
                                Thread.sleep(15);
                        }
                        catch (InterruptedException e)
                        {
                                e.printStackTrace();
                        }
                }
        }
}
