package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sun.applet.Main;

public class SettingsMenuController extends MenuController {


    @FXML
    private Slider gameSoundSlider;
    @FXML
    private Slider musicSlider;

    static MediaPlayer mediaPlayer;

    public void initialize(){
        musicSlider.setValue(mediaPlayer.getVolume()*100);
    }
    @FXML
    private void changeVolume(){
        mediaPlayer.setVolume(musicSlider.getValue()/100);
    }

    static protected void setMyMedia(MediaPlayer mp){
        mediaPlayer = mp;
    }


}
