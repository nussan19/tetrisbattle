import javax.swing.*;
import java.awt.*;

public class ScoreChallengeMode extends JPanel {
    private SoundManager sound = new SoundManager();

    public ScoreChallengeMode(JFrame frame) {
        sound.playBGM("sound/gameBGM.wav", true);

        setLayout(new BorderLayout());

        // スコア表示
        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.add(scoreLabel, BorderLayout.CENTER);

        // プレイ画面を中央にするために中央パネルを用意
        JPanel centerPanel = new JPanel(new GridBagLayout());
        PlayerBoard playerBoard = new PlayerBoard();
        playerBoard.setScoreUpdateListener(score -> scoreLabel.setText("Score: " + score));
        centerPanel.add(playerBoard);  // プレイヤーボードを真ん中に

        // 戻るボタン
        JButton backButton = new JButton("タイトルに戻る");
        backButton.addActionListener(e -> {
            sound.stopBGM();
            Main.showTitleScreen(frame);
        });

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER); // ← パネルで囲って中央揃え！
        add(backButton, BorderLayout.SOUTH);

        // フォーカスを与えて操作可能にする
        SwingUtilities.invokeLater(() -> playerBoard.requestFocusInWindow());
    }
}