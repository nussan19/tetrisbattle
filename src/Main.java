import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 700);
            frame.setLocationRelativeTo(null);
            showTitleScreen(frame);
            frame.setVisible(true);
        });
    }

    // タイトル画面表示
    public static void showTitleScreen(JFrame frame) {
        frame.setContentPane(new TitleScreen(frame));
        frame.revalidate();
        frame.repaint();
        frame.getContentPane().requestFocusInWindow();
    }

    // モード選択画面表示
    public static void showModeScreen(JFrame frame) {
        frame.setContentPane(new ModeScreen(frame));
        frame.revalidate();
        frame.repaint();
        frame.getContentPane().requestFocusInWindow();
    }
}