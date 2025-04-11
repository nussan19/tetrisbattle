import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AiBattleMode extends JPanel {
    private SoundManager sound = new SoundManager();
    private Timer checkTimer;

    public AiBattleMode(JFrame frame) {
        sound.playBGM("sound/gameBGM.wav", true);

        setLayout(new BorderLayout());

        PlayerBoard playerBoard = new PlayerBoard();
        AiBoard aiBoard = new AiBoard();

        add(playerBoard, BorderLayout.WEST);
        add(aiBoard, BorderLayout.EAST);

        JButton backButton = new JButton("タイトルに戻る");
        backButton.addActionListener(e -> {
            stopAllSound();
            Main.showTitleScreen(frame);
        });
        add(backButton, BorderLayout.SOUTH);

        // ⭐ フォーカス設定はイベントディスパッチスレッドで遅延呼び出し
        SwingUtilities.invokeLater(() -> playerBoard.requestFocusInWindow());

        // 勝敗チェックタイマー
        checkTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aiBoard.isGameOver()) {
                    stopAllSound();
                    sound.playSE("sound/win.wav");
                    showResult("WIN!", frame);
                    checkTimer.stop();
                } else if (playerBoard.isGameOver()) {
                    stopAllSound();
                    sound.playSE("sound/lose.wav");
                    showResult("LOSE...", frame);
                    checkTimer.stop();
                }
            }
        });
        checkTimer.start();
    }

    private void stopAllSound() {
        sound.stopBGM();
    }

    private void showResult(String message, JFrame frame) {
        JLabel resultLabel = new JLabel(message);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 48));
        resultLabel.setForeground(Color.YELLOW);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        removeAll();
        setLayout(new BorderLayout());
        add(resultLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("タイトルに戻る");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.addActionListener(e -> Main.showTitleScreen(frame));
        add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}