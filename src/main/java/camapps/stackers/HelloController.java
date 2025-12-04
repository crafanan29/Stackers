package camapps.stackers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(Event event) throws IOException
    {
        welcomeText.setText("Welcome to JavaFX Application!");

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow(); // Another scene
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("play-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("It worked!");
        stage.setScene(scene);

    }
}
