import javax.swing.*;
import java.awt.*;

public class ModeScreen extends JPanel {
    private SoundManager sound = new SoundManager();

    public ModeScreen(JFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        // タイトルBGM再生
        sound.playBGM("sound/titleBGM.wav", true);

        JLabel label = new JLabel("モードを選んでください");
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(0, 255, 255)); // ネオンブルー
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(50, 0, 40, 0));

        JButton aiBattleButton = createNeonButton("AIバトルモード", () -> {
            sound.stopBGM(); // BGM止める
            frame.setContentPane(new AiBattleMode(frame));
            frame.revalidate();
        });

        JButton scoreButton = createNeonButton("スコアチャレンジ", () -> {
            sound.stopBGM();
            frame.setContentPane(new ScoreChallengeMode(frame));
            frame.revalidate();
        });

        JButton backButton = createNeonButton("タイトルに戻る", () -> {
            sound.stopBGM();
            Main.showTitleScreen(frame);
        });

        add(Box.createVerticalGlue());
        add(label);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(aiBattleButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(scoreButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(backButton);
        add(Box.createVerticalGlue());
    }

    private JButton createNeonButton(String text, Runnable onClick) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(new Color(255, 0, 255)); // ネオンピンク
        button.setBackground(new Color(20, 20, 20));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 2));
        button.setMaximumSize(new Dimension(260, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> onClick.run());
        return button;
    }
}