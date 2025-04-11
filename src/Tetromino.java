import java.awt.*;

public class Tetromino {
    public int type;
    public int rotation;

    private static final Point[][][] SHAPES = {
            { {p(0,1), p(1,1), p(2,1), p(3,1)}, {p(2,0), p(2,1), p(2,2), p(2,3)} },
            { {p(0,0), p(1,0), p(0,1), p(1,1)} },
            { {p(1,0), p(0,1), p(1,1), p(2,1)}, {p(1,0), p(1,1), p(2,1), p(1,2)},
                    {p(0,1), p(1,1), p(2,1), p(1,2)}, {p(1,0), p(0,1), p(1,1), p(1,2)} },
            { {p(1,0), p(2,0), p(0,1), p(1,1)}, {p(1,0), p(1,1), p(2,1), p(2,2)} },
            { {p(0,0), p(1,0), p(1,1), p(2,1)}, {p(2,0), p(1,1), p(2,1), p(1,2)} },
            { {p(0,0), p(0,1), p(1,1), p(2,1)}, {p(1,0), p(2,0), p(1,1), p(1,2)},
                    {p(0,1), p(1,1), p(2,1), p(2,2)}, {p(1,0), p(1,1), p(0,2), p(1,2)} },
            { {p(2,0), p(0,1), p(1,1), p(2,1)}, {p(1,0), p(1,1), p(1,2), p(2,2)},
                    {p(0,1), p(1,1), p(2,1), p(0,2)}, {p(0,0), p(1,0), p(1,1), p(1,2)} }
    };

    public Tetromino(int type) {
        this.type = type;
        this.rotation = 0;
    }

    public Point[] shape() {
        Point[] s = SHAPES[type - 1][rotation % SHAPES[type - 1].length];
        Point[] copy = new Point[4];
        for (int i = 0; i < 4; i++) {
            copy[i] = new Point(s[i]);
        }
        return copy;
    }

    // 🔁 プレイヤー用：1回転
    public Tetromino rotate() {
        return rotate(1);
    }

    // 🔁 AI用：複数回回転
    public Tetromino rotate(int times) {
        Tetromino t = new Tetromino(this.type);
        t.rotation = this.rotation + times;
        return t;
    }

    public static Color getColor(int type) {
        return switch (type) {
            case 1 -> Color.CYAN;
            case 2 -> Color.YELLOW;
            case 3 -> Color.MAGENTA;
            case 4 -> Color.GREEN;
            case 5 -> Color.RED;
            case 6 -> Color.BLUE;
            case 7 -> Color.ORANGE;
            default -> Color.GRAY;
        };
    }

    private static Point p(int x, int y) {
        return new Point(x, y);
    }
}