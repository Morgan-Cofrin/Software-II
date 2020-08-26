package Main;

import Model.InformationInventory;
import ViewController.MainScreens.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import static utils.DBConnection.closeConnection;
import static utils.DBConnection.startConnection;
import static utils.SQL_ListFillers.*;

public class Main extends Application {


    public static void main(String[] args) {

        try {
            startConnection();

            launch(args);

            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage stage) throws Exception {

        try {
            InformationInventory inv = new InformationInventory();
            addTestData(inv);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/MainScreens/LoginScreen.fxml"));
            LoginScreenController controller = new LoginScreenController(inv);

            //Language swap for login screen
            if (Locale.getDefault().getLanguage().equals("es") || (Locale.getDefault().getLanguage().equals("en"))) {

                ResourceBundle resourceBundle = ResourceBundle.getBundle("utils/Nat", Locale.getDefault());
                loader.setResources(resourceBundle);
            }

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addTestData(InformationInventory inv) throws SQLException {
        populateUserList(inv);
        populateCustomerList(inv);
        populateAppointmentList(inv);
    }


}