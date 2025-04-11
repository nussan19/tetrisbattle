import javax.swing.*;
import java.awt.*;

public class TitleScreen extends JPanel {
    private SoundManager sound = new SoundManager();

    public TitleScreen(JFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        // タイトルBGM 再生
        sound.playBGM("sound/titleBGM.wav", true);

        JLabel title = new JLabel("TETRIS BATTLE");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0, 255, 255)); // ネオンブルー
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 40, 0));
        // タイトルにネオンっぽいキラキラアニメーション
        new Timer(100, e -> {
            int r = 100 + (int)(Math.random() * 155);
            int g = 100 + (int)(Math.random() * 155);
            int b = 255;
            title.setForeground(new Color(r, g, b));
        }).start();

        JButton startButton = createButton("ゲームスタート", () -> {
            sound.stopBGM(); // BGM停止
            Main.showModeScreen(frame);
        });

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        add(Box.createVerticalGlue());
    }

    private JButton createButton(String text, Runnable onClick) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(new Color(255, 0, 255)); // ネオンピンク
        button.setBackground(new Color(20, 20, 20));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 2));
        button.setMaximumSize(new Dimension(240, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> onClick.run());
        return button;
    }
}