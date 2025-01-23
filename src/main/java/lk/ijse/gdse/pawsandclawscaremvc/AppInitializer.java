package lk.ijse.gdse.pawsandclawscaremvc;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

public class AppInitializer extends Application {
    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent load = FXMLLoader.load(getClass().getResource("/view/loading.fxml"));
        stage.setScene(new Scene(load));
        stage.show();

        Task<Scene> loadingTask = new Task<Scene>() {

            @Override
            protected Scene call() throws Exception {
                FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/LoginPage.fxml"));
                return new Scene(fxmlLoader.load());
            }
        };

        loadingTask.setOnSucceeded(event -> {
            Scene value = loadingTask.getValue();

            stage.setTitle("Paws & Claws Care");
            stage.setResizable(false);
            stage.setScene(value);
        });

        new Thread(loadingTask).start();

    }

}
