import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private Clip bgmClip;
    private boolean isBGMPlaying = false;

    public void playBGM(String path, boolean loop) {
        if (isBGMPlaying) return; // ★ すでに流れてたら再生しない！

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioStream);
            if (loop) {
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                bgmClip.start();
            }
            isBGMPlaying = true;
        } catch (Exception e) {
            System.err.println("BGMエラー: " + e.getMessage());
        }
    }

    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
            isBGMPlaying = false; // ★ 停止したらリセット！
        }
    }

    public void playSE(String path) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip seClip = AudioSystem.getClip();
            seClip.open(audioStream);
            seClip.start();
        } catch (Exception e) {
            System.err.println("SEエラー: " + e.getMessage());
        }
    }
}