package frontend;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Audio {
    private MediaPlayer gameMusic;
    private MediaPlayer cashSound;
    private MediaPlayer diceSound;
    private MediaPlayer moveSound;
    private MediaPlayer policeSound;
    private MediaPlayer wheelSound;

    private static Audio obj;

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

    public static Audio getInstance () {
        if(obj == null)
            obj = new Audio();
        return obj;
    }

    public void setVolumeOfGameMusic (double volume){
        gameMusic.setVolume(volume);
    }

    public void setVolumeOfGameSound (double volume) {
        cashSound.setVolume(volume);
        diceSound.setVolume(volume);
        moveSound.setVolume(volume);
        policeSound.setVolume(volume);
        wheelSound.setVolume(volume);
    }

    public void playGameMusic () {
        gameMusic.setAutoPlay(true);
    }

    public void playCashSound () {
        cashSound.setAutoPlay(true);
    }

    public void playDiceSound () {
        diceSound.setAutoPlay(true);
    }

    public void playMoveSound () {
        moveSound.setAutoPlay(true);
    }

    public void playPoliceSound () {
        policeSound.setAutoPlay(true);
    }

    public void playWheelSound () {
        wheelSound.setAutoPlay(true);
    }

}
