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

    static MediaPlayer mediaPlayer;
    @Setter
    static MediaPlayer moveEffect;
    @Setter
    static MediaPlayer cashEffect;
    @Setter
    static MediaPlayer spinEffect;
    @Setter
    static MediaPlayer jailEffect;
    @Setter
    static MediaPlayer diceEffect;


    public void initialize(){
        musicSlider.setValue(mediaPlayer.getVolume()*100);
        gameSoundSlider.setValue(50);


    }

    @FXML
    private void changeVolume(){
        mediaPlayer.setVolume(musicSlider.getValue()/100);
    }

    static protected void setMyMedia(MediaPlayer mp){
        mediaPlayer = mp;
    }

}
