package Snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    static final int ScreenWidth = 600;
    static final int ScreenHeight = 600;
    static final int UNIT_SIZE = 25;
    static final int GameUnits = (ScreenWidth * ScreenHeight) / (UNIT_SIZE * UNIT_SIZE);
    static final int Delay = 75;
    final int x[] = new int[GameUnits];
    final int y[] = new int[GameUnits];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // הנחש מתחיל לנוע ימינה
    boolean running = false;
    Random random;
    Timer timer;
    // Graphics graphics;
    int currentDimension = 0; // להחזיק את המימד הנוכחי


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        newApple();
        running = true;
        timer = new Timer(Delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                if (x[0] < 0) {
                    changeDimension();
                    x[0] = ScreenWidth - UNIT_SIZE;
                }
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                if (x[0] >= ScreenWidth) {
                    changeDimension();
                    x[0] = 0;
                }
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                if (y[0] < 0) {
                    changeDimension();
                    y[0] = ScreenHeight - UNIT_SIZE;
                }
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                if (y[0] >= ScreenHeight) {
                    changeDimension();
                    y[0] = 0;
                }
                break;
        }
        System.out.println("Snake moved to: (" + x[0] + ", " + y[0] + ")");
    }

    private void changeDimension() {
        currentDimension++;
        this.setBackground(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        newApple();
    }

    public void checkFood() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            appleEaten++;
            newApple();
            System.out.println("Food eaten at: (" + appleX + ", " + appleY + ")");
        }
    }

    public void draw(Graphics graphics) {
        for (int i = 0; i < ScreenHeight / UNIT_SIZE; i++) {
            graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, ScreenHeight);
            graphics.drawLine(0, i * UNIT_SIZE, ScreenWidth, i * UNIT_SIZE);
        }

        if (running) {
            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            graphics.setColor(Color.magenta);
            graphics.fillOval(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 1; i < bodyParts; i++) {
                graphics.setColor(new Color(40, 200, 150));
                graphics.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics =  getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + appleEaten, (ScreenWidth - metrics.stringWidth("Score: " + appleEaten)) / 2, graphics.getFont().getSize());

        } else {
            gameOver(graphics);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (ScreenWidth / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (ScreenHeight / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkHit() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                System.out.println("Snake hit itself.");
            }
        }

        if (!running) {
            System.out.println("Game over. Score: " + appleEaten);
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (ScreenWidth - metrics.stringWidth("Game Over")) / 2, ScreenHeight / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + appleEaten, (ScreenWidth - metrics.stringWidth("Score: " + appleEaten)) / 2, graphics.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
