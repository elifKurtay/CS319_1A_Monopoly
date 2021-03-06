package frontend;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;

import java.io.File;

public class Audio {
    @Getter private MediaPlayer gameMusic;
    @Getter private MediaPlayer cashSound;
    private MediaPlayer diceSound;
    private MediaPlayer moveSound;
    private MediaPlayer policeSound;
    private MediaPlayer wheelSound;

    private static Audio obj;

    /**
     * Default Constructor of Audio initializes the sound effects
     */
    private Audio(){

        //initialization of game music
        String path = "assets\\music\\music.mp3";
        Media media = new Media(new File(path).toURI().toString());

        gameMusic = new MediaPlayer(media);
        gameMusic.setVolume(0);

        gameMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                gameMusic.seek(Duration.ZERO);
            }
        });

        //initialization of cash sound
        path = "assets\\music\\cashSound.mp3";
        media = new Media(new File(path).toURI().toString());

        cashSound = new MediaPlayer(media);
        cashSound.setVolume(0.5);


        //initialization of dice sound
        path = "assets\\music\\diceSound.mp3";
        media = new Media(new File(path).toURI().toString());

        diceSound = new MediaPlayer(media);
        diceSound.setVolume(0.5);


        //initialization of move sound
        path = "assets\\music\\moveSound.mp3";
        media = new Media(new File(path).toURI().toString());

        moveSound = new MediaPlayer(media);
        moveSound.setVolume(0.5);


        //initialization of police sound
        path = "assets\\music\\policeSound.mp3";
        media = new Media(new File(path).toURI().toString());

        policeSound = new MediaPlayer(media);
        policeSound.setVolume(0.5);


        //initialization of wheel sound
        path = "assets\\music\\wheelSound.mp3";
        media = new Media(new File(path).toURI().toString());

        wheelSound = new MediaPlayer(media);
        wheelSound.setVolume(0.5);
    }

    /**
     * Audio is a singleton and this method allows access to it
     * @return The singleton instance of Audio
     */
    public static Audio getInstance () {
        if(obj == null)
            obj = new Audio();
        return obj;
    }

    /**
     * Sets the volume of game music. Its effect will be clamped to the range [0.0, 1.0].
     * @param volume Volume percentage denoted as [0.0, 1.0]
     */
    public void setVolumeOfGameMusic (double volume){
        gameMusic.setVolume(volume);
    }

    /**
     * Sets the volume of all sound effects. Its effect will be clamped to the range [0.0, 1.0].
     * @param volume Volume percentage denoted as [0.0, 1.0]
     */
    public void setVolumeOfGameSound (double volume) {
        cashSound.setVolume(volume);
        diceSound.setVolume(volume);
        moveSound.setVolume(volume);
        policeSound.setVolume(volume);
        wheelSound.setVolume(volume);
    }

    /**
     * Auto-plays the music
     */
    public void playGameMusic () {
        gameMusic.setAutoPlay(true);
    }

    /**
     * Plays the cash sound
     */
    public void playCashSound () {
        cashSound.seek(Duration.ZERO);
        cashSound.setAutoPlay(true);
    }

    /**
     * Plays the dice sound
     */
    public void playDiceSound () {
        diceSound.seek(Duration.ZERO);
        diceSound.play();
    }

    /**
     * Plays the move sound
     */
    public void playMoveSound () {
        moveSound.seek(Duration.ZERO);
        moveSound.setAutoPlay(true);
    }

    /**
     * Plays the police sound
     */
    public void playPoliceSound () {
        policeSound.seek(Duration.ZERO);
        policeSound.setRate(2);
        policeSound.play();
    }

    /**
     * Plays the wheel of fortune sound
     */
    public void playWheelSound () {
        wheelSound.seek(Duration.ZERO);
        wheelSound.setRate(2);
        wheelSound.play();
    }

}
