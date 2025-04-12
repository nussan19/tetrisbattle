import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AiBoard extends JPanel implements ActionListener {
    private final int WIDTH = 10, HEIGHT = 20, CELL = 25;
    private javax.swing.Timer timer;
    private int[][] field = new int[HEIGHT][WIDTH];
    private Tetromino current;
    private Tetromino nextBlock = new Tetromino(new Random().nextInt(7) + 1); // 初期化
    private int blockX, blockY;
    private Random rand = new Random();
    private boolean gameOver = false;

    public AiBoard() {
        setPreferredSize(new Dimension(WIDTH * CELL, HEIGHT * CELL));
        setBackground(Color.DARK_GRAY);
        spawnBlock();
        timer = new javax.swing.Timer(100, this);
        timer.start();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void spawnBlock() {
        current = nextBlock;
        nextBlock = new Tetromino(rand.nextInt(7) + 1);
        blockX = WIDTH / 2 - 2;
        blockY = 0;

        int bestScore = Integer.MIN_VALUE;
        int bestX = 0;
        int bestRotation = 0;

        for (int rot1 = 0; rot1 < 4; rot1++) {
            Tetromino t1 = current;
            for (int i = 0; i < rot1; i++) t1 = t1.rotate();

            for (int x1 = -2; x1 < WIDTH + 2; x1++) {
                int y1 = 0;
                while (!collides(x1, y1 + 1, t1)) y1++;
                if (collides(x1, y1, t1)) continue;

                int[][] temp1 = copyField();
                placeBlock(temp1, x1, y1, t1);
                clearLines(temp1);

                int maxScore = Integer.MIN_VALUE;

                for (int rot2 = 0; rot2 < 4; rot2++) {
                    Tetromino t2 = nextBlock;
                    for (int i = 0; i < rot2; i++) t2 = t2.rotate();

                    for (int x2 = -2; x2 < WIDTH + 2; x2++) {
                        int y2 = 0;
                        while (!collides(temp1, x2, y2 + 1, t2)) y2++;
                        if (collides(temp1, x2, y2, t2)) continue;

                        int[][] temp2 = copyField(temp1);
                        placeBlock(temp2, x2, y2, t2);
                        clearLines(temp2);

                        int score = evaluateField(temp2, t1);
                        maxScore = Math.max(maxScore, score);
                    }
                }

                if (maxScore > bestScore) {
                    bestScore = maxScore;
                    bestX = x1;
                    bestRotation = rot1;
                }
            }
        }

        for (int i = 0; i < bestRotation; i++) current = current.rotate();
        blockX = bestX;
        blockY = 0;

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

    private boolean collides(int[][] board, int x, int y, Tetromino t) {
        for (Point p : t.shape()) {
            int px = x + p.x;
            int py = y + p.y;
            if (px < 0 || px >= WIDTH || py >= HEIGHT) return true;
            if (py >= 0 && board[py][px] != 0) return true;
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

    private void clearLines(int[][] board) {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < WIDTH; x++) {
                if (board[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                for (int r = y; r > 0; r--) {
                    board[r] = Arrays.copyOf(board[r - 1], WIDTH);
                }
                board[0] = new int[WIDTH];
                y++;
            }
        }
    }

    private void clearLines() {
        clearLines(field);
    }

    private int[][] copyField() {
        return copyField(field);
    }

    private int[][] copyField(int[][] source) {
        int[][] copy = new int[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            copy[y] = Arrays.copyOf(source[y], WIDTH);
        }
        return copy;
    }

    private void placeBlock(int[][] board, int x, int y, Tetromino t) {
        for (Point p : t.shape()) {
            int px = x + p.x;
            int py = y + p.y;
            if (px >= 0 && px < WIDTH && py >= 0 && py < HEIGHT) {
                board[py][px] = t.type;
            }
        }
    }

    private int evaluateField(int[][] board, Tetromino lastPlaced) {
        int holes = 0, height = 0, linesCleared = 0, touching = 0;

        for (int x = 0; x < WIDTH; x++) {
            boolean blockFound = false;
            for (int y = 0; y < HEIGHT; y++) {
                if (board[y][x] != 0) {
                    blockFound = true;
                } else if (blockFound) {
                    holes++;
                }
            }
        }

        for (int y = 0; y < HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < WIDTH; x++) {
                if (board[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) linesCleared++;
        }

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (board[y][x] != 0) {
                    height = HEIGHT - y;
                    break;
                }
            }
        }

        for (Point p : lastPlaced.shape()) {
            int px = blockX + p.x;
            int py = blockY + p.y + 1;
            if (py >= HEIGHT || (py >= 0 && board[py][px] != 0)) {
                touching++;
            }
        }

        int score = 0;
        score += (linesCleared == 4) ? 2000 : linesCleared * 300;
        score -= holes * 60;
        score -= height * 2;
        score += touching * 10;

        return score;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        drop();
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
}