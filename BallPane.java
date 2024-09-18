import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics2D;

import javax.swing.Timer;
import javax.swing.JPanel;
import java.security.SecureRandom;

import java.util.concurrent.locks.ReentrantLock;

public class BallPane extends JPanel 
{
        // Possibly Replace with getWidth
        private final int SET_COLORS = 10;
        private final int ANIMATION_CYCLE = 25;
        private Color[] colors = {Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, 
                Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.YELLOW};
        private int ballCount;
        private Ball[] balls;
        private Thread[] threads;
        public static ReentrantLock lock = new ReentrantLock(true); // guarantee fairness
        public static int completedCycles = 0;
        private Timer timer;


        // Methods
        // call AFTER pane has been added to JFrame
        // Allows for sizing
        public void addBalls(int ballCount){
                this.ballCount = ballCount;

                balls = new Ball[ballCount];
                threads = new Thread[ballCount];

                int i, j;

                if (ballCount < SET_COLORS)
                {
                        for (i = 0; i < ballCount; ++i)
                        {
                                Color ballColor = colors[i];
                                balls[i] = new Ball(ballColor, getWidth(), getHeight());
                        }
                }

                else
                {
                        for (i = 0; i < SET_COLORS; ++i)
                        {
                                Color ballColor = colors[i];
                                balls[i] = new Ball(ballColor, getWidth(), getHeight());
                        }

                        // Use random colors after 10
                        for (j = 0; j < (ballCount - SET_COLORS); ++j)
                        {
                                Color ballColor = randomColor();
                                balls[SET_COLORS + j] = new Ball(ballColor, getWidth(), getHeight());
                        }
                }

                for (i = 0; i < ballCount; ++i)
                {
                        threads[i] = new Thread(balls[i]);
                }

                timer = new Timer(ANIMATION_CYCLE, new ActionListener() 
                {
                        @Override
                        public void actionPerformed(ActionEvent e) 
                        {
                                detectCollision();
                                repaint();
                        }
                });
        }

        private Color randomColor()
        {
                SecureRandom randomNum = new SecureRandom();

                int red = randomNum.nextInt(256);
                int green = randomNum.nextInt(256);
                int blue = randomNum.nextInt(256);

                return new Color(red, green, blue);
        }


        public void startBalls()
        {
                for (Thread thread : threads)
                {
                        thread.start();
                }

                timer.start();
        }

        public void detectCollision(){

                lock.lock();
                try{
                        while (completedCycles < ballCount)
                                wait();
                        
                        completedCycles = 0;

                        for (Ball ball : balls){
                                ball.getCenter();
                        }

                        for (int i = 0; i < ballCount; ++i){
                                for (int j = i + 1; j < ballCount; ++i){
                                        if (Math.abs(balls[i].centX - balls[j].centX) < balls[i].ballDiameter){
                                                if (balls[i].centX > balls[j].centX){
                                                        balls[i].bounceRight();
                                                        balls[j].bounceLeft();
                                                }
                                                else{
                                                        balls[i].bounceLeft();
                                                        balls[j].bounceRight();
                                                }

                                        }
                                        if (Math.abs(balls[i].centY - balls[j].centY) < balls[i].ballDiameter){
                                                if (balls[i].centY > balls[j].centY){
                                                        balls[i].bounceDown();
                                                        balls[j].bounceUp();
                                                }
                                                else{
                                                        balls[i].bounceUp();
                                                        balls[j].bounceDown();
                                                }
                                        }
                                }
                        }
                } catch (InterruptedException e) {
                        e.printStackTrace();
                } finally {
                        notifyAll();
                        lock.unlock();
                }
        }

        // Overrides
        @Override 
        public void paintComponent(Graphics g)
        {
                super.paintComponent(g);

                for (Ball ball : balls)
                {
                        Graphics2D g2d = (Graphics2D) g.create();

                        // For super HD balls
                        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                        rh.put(RenderingHints.KEY_RENDERING,
                                        RenderingHints.VALUE_RENDER_QUALITY);
                        g2d.setRenderingHints(rh);

                        ball.paint(g2d);
                        g2d.dispose();
                }
        }
}
