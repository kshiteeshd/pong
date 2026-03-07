import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    Clip clip;
    public void playSound(String path){
        try{
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void loopSound(String path)
    {
        try
        {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopSound()
    {
        if(clip != null)
        {
            clip.stop();
            clip.close();
        }
    }
}




