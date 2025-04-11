import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class PlayerBoard extends JPanel implements KeyListener, ActionListener {
    private final int WIDTH = 10, HEIGHT = 20, CELL = 25;
    private Timer timer;
    private int[][] field = new int[HEIGHT][WIDTH];
    private Tetromino current;
    private int blockX, blockY;
    private Random rand = new Random();
    private SoundManager sound = new SoundManager();
    private int score = 0;
    private ScoreUpdateListener scoreUpdateListener;

    public PlayerBoard() {
        setPreferredSize(new Dimension(WIDTH * CELL, HEIGHT * CELL));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        spawnBlock();
        timer = new Timer(500, this);
        timer.start();
    }

    private void spawnBlock() {
        current = new Tetromino(rand.nextInt(7) + 1);
        blockX = WIDTH / 2 - 2;
        blockY = -1;
        if (collides(blockX, blockY, current)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "ゲームオーバー");
        }
    }

    private boolean collides(int x, int y, Tetromino t) {
        for (Point p : t.shape()) {
            int px = x + p.x;
            int py = y + p.y;
            if (px < 0 || px >= WIDTH || py >= HEIGHT) return true;
            if (py >= 0 && field[py][px] != 0) return true;
        }
        return false;
    }

    private void fixToField() {
        for (Point p : current.shape()) {
            int px = blockX + p.x;
            int py = blockY + p.y;
            if (py >= 0) field[py][px] = current.type;
        }
    }

    private void clearLines() {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < WIDTH; x++) {
                if (field[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                for (int r = y; r > 0; r--) {
                    field[r] = Arrays.copyOf(field[r - 1], WIDTH);
                }
                field[0] = new int[WIDTH];
                score += 100;
                if (scoreUpdateListener != null) {
                    scoreUpdateListener.onScoreUpdated(score);
                }
                sound.playSE("sound/kieru.wav"); // ライン消去音
                y++;
            }
        }
    }

    private void drop() {
        if (!collides(blockX, blockY + 1, current)) {
            blockY++;
        } else {
            fixToField();
            sound.playSE("sound/block.wav"); // ブロック固定音（ここ！）
            clearLines();
            spawnBlock();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        for (int y = 0; y <= HEIGHT; y++) {
            for (int x = 0; x <= WIDTH; x++) {
                g.drawRect(x * CELL, y * CELL, CELL, CELL);
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (field[y][x] != 0) {
                    g.setColor(Tetromino.getColor(field[y][x]));
                    g.fillRect(x * CELL, y * CELL, CELL, CELL);
                }
            }
        }

        g.setColor(Tetromino.getColor(current.type));
        for (Point p : current.shape()) {
            int px = blockX + p.x;
            int py = blockY + p.y;
            if (py >= 0) g.fillRect(px * CELL, py * CELL, CELL, CELL);
        }
    }

    @Override public void actionPerformed(ActionEvent e) { drop(); }

    @Override public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && !collides(blockX - 1, blockY, current)) blockX--;
        else if (key == KeyEvent.VK_RIGHT && !collides(blockX + 1, blockY, current)) blockX++;
        else if (key == KeyEvent.VK_DOWN) drop();
        else if (key == KeyEvent.VK_UP) {
            Tetromino rotated = current.rotate();
            if (!collides(blockX, blockY, rotated)) current = rotated;
        } else if (key == KeyEvent.VK_SPACE) {
            while (!collides(blockX, blockY + 1, current)) blockY++;
            drop();
        }
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    // スコア通知
    public void setScoreUpdateListener(ScoreUpdateListener listener) {
        this.scoreUpdateListener = listener;
    }

    public boolean isGameOver() {
        return timer == null || !timer.isRunning();
    }

    public int getScore() {
        return score;
    }
}