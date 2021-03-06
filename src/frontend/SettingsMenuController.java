package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import sun.applet.Main;

import java.io.File;

public class SettingsMenuController extends MenuController {


    @FXML
    private Slider gameSoundSlider;
    @FXML
    private Slider musicSlider;

    private Audio obj =  Audio.getInstance();

    /**
     * Initializes the controller
     */
    public void initialize(){
        musicSlider.setValue(obj.getGameMusic().getVolume()*100);
        gameSoundSlider.setValue(obj.getCashSound().getVolume()*100);
    }

    /**
     * Changes the music volume
     */
    @FXML
    private void changeGameMusicVolume(){
        obj.setVolumeOfGameMusic(musicSlider.getValue()/100);
    }

    /**
     * Changes the sound effect volume
     */
    @FXML
    private void changeGameSoundVolume(){
        obj.setVolumeOfGameSound(gameSoundSlider.getValue()/100);
    }


}
