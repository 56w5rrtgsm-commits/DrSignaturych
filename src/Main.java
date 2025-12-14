import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        stage.setScene(new Scene(loader.load(), 800, 500));
        stage.setTitle("Signature Scanner");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
