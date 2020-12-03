package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import sun.applet.Main;

public class SettingsMenuController {

    @FXML
    private Slider gameSoundSlider;
    @FXML
    private Slider musicSlider;

    @FXML
    protected void backButtonAction(ActionEvent event) {
        MainMenuController.mediaPlayer.setVolume(0.5);
    }



    @FXML
    private void changeVolume(){
        MainMenuController.mediaPlayer.setVolume(musicSlider.getValue()/100);
    }

}
