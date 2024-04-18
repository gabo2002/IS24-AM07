package it.polimi.ingsw.am07;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField nicknameField;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome " + nicknameField.getText());
    }
}