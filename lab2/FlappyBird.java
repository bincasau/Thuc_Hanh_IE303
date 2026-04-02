import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FlappyBird extends JFrame {
    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(360, 640);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new GamePanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBird::new);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 360;
    private final int HEIGHT = 640;

    private final double GRAVITY = 0.5;
    private final double JUMP_STRENGTH = -8.0;
    private final int PIPE_SPEED = 3;
    private final int PIPE_GAP = 170;
    private final int PIPE_WIDTH = 70;
    private final int PIPE_SPAWN_DELAY = 90;
    private final int GROUND_MARGIN = 0;

    private Image bgImage;
    private Image birdImage;
    private Image topPipeImage;
    private Image bottomPipeImage;

    private Bird bird;
    private ArrayList<PipePair> pipes;
    private Timer timer;
    private Random random;

    private boolean gameOver = false;
    private int score = 0;
    private int frameCount = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        bgImage = new ImageIcon("flappybirdbg.png").getImage();
        birdImage = new ImageIcon("flappybird.png").getImage();
        topPipeImage = new ImageIcon("toppipe.png").getImage();
        bottomPipeImage = new ImageIcon("bottompipe.png").getImage();

        random = new Random();
        initGame();

        timer = new Timer(20, this);
        timer.start();
    }

    private void initGame() {
        bird = new Bird(80, 250, 40, 30);
        pipes = new ArrayList<>();
        gameOver = false;
        score = 0;
        frameCount = 0;
    }

    private void spawnPipe() {
        int gapY = 140 + random.nextInt(260); // vị trí khoảng trống
        pipes.add(new PipePair(WIDTH, gapY, PIPE_GAP, PIPE_WIDTH));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, this);

        for (PipePair pipe : pipes) {
            // Ống trên
            g.drawImage(
                    topPipeImage,
                    pipe.x,
                    0,
                    PIPE_WIDTH,
                    pipe.topHeight,
                    this
            );

            // Ống dưới
            g.drawImage(
                    bottomPipeImage,
                    pipe.x,
                    pipe.bottomY,
                    PIPE_WIDTH,
                    HEIGHT - pipe.bottomY - GROUND_MARGIN,
                    this
            );
        }

        g.drawImage(birdImage, (int) bird.x, (int) bird.y, bird.width, bird.height, this);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Score: " + score, 20, 40);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER", 80, 280);

            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Nhấn R để chơi lại", 95, 320);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        frameCount++;

        bird.velocity += GRAVITY;
        bird.y += bird.velocity;

        if (frameCount % PIPE_SPAWN_DELAY == 0) {
            spawnPipe();
        }

        Iterator<PipePair> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            PipePair pipe = iterator.next();
            pipe.x -= PIPE_SPEED;

            if (!pipe.scored && pipe.x + PIPE_WIDTH < bird.x) {
                pipe.scored = true;
                score++;
            }

            if (pipe.x + PIPE_WIDTH < 0) {
                iterator.remove();
            }
        }

        checkCollision();
    }

    private void checkCollision() {
        Rectangle birdRect = new Rectangle((int) bird.x, (int) bird.y, bird.width, bird.height);

        if (bird.y < 0 || bird.y + bird.height > HEIGHT) {
            gameOver = true;
            return;
        }

        for (PipePair pipe : pipes) {
            Rectangle topRect = new Rectangle(pipe.x, 0, PIPE_WIDTH, pipe.topHeight);
            Rectangle bottomRect = new Rectangle(pipe.x, pipe.bottomY, PIPE_WIDTH, HEIGHT - pipe.bottomY);

            if (birdRect.intersects(topRect) || birdRect.intersects(bottomRect)) {
                gameOver = true;
                return;
            }
        }
    }

    private void restartGame() {
        initGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!gameOver && (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER)) {
            bird.velocity = JUMP_STRENGTH;
        }

        if (gameOver && key == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}

class Bird {
    double x, y;
    double velocity;
    int width, height;

    public Bird(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = 0;
    }
}

class PipePair {
    int x;
    int topHeight;
    int bottomY;
    boolean scored = false;

    public PipePair(int startX, int gapY, int gapSize, int pipeWidth) {
        this.x = startX;
        this.topHeight = gapY - gapSize / 2;
        this.bottomY = gapY + gapSize / 2;
    }
}