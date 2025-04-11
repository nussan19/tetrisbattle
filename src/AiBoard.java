import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AiBoard extends JPanel implements ActionListener {
    private final int WIDTH = 10, HEIGHT = 20, CELL = 25;
    private javax.swing.Timer timer;
    private int[][] field = new int[HEIGHT][WIDTH];
    private Tetromino current;
    private int blockX, blockY;
    private Random rand = new Random();
    private boolean gameOver = false;

    public AiBoard() {
        setPreferredSize(new Dimension(WIDTH * CELL, HEIGHT * CELL));
        setBackground(Color.DARK_GRAY);
        spawnBlock();
        timer = new javax.swing.Timer(500, this);
        timer.start();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void spawnBlock() {
        current = new Tetromino(rand.nextInt(7) + 1);
        blockX = WIDTH / 2 - 2;
        blockY = -1;
        if (collides(blockX, blockY, current)) {
            timer.stop();
            gameOver = true;
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

    private void drop() {
        if (!collides(blockX, blockY + 1, current)) {
            blockY++;
        } else {
            fixToField();
            clearLines();
            spawnBlock();
        }
        repaint();
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
                y++;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        drop();
    }
}