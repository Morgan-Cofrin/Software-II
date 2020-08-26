package utils;

//Helper class to change scenes in the main application

import Model.InformationInventory;
import ViewController.MainScreens.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class ScreenNavigators {

    InformationInventory inv;


    public ScreenNavigators(InformationInventory inv) {
        this.inv = inv;
    }


    public void navigateLoginScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenNavigators.class.getResource("/ViewController/MainScreens/LoginScreen.fxml"));
            LoginScreenController controller = new LoginScreenController(inv);

            //Language swap for login screen
            if(Locale.getDefault().getLanguage().equals("es") || (Locale.getDefault().getLanguage().equals("en"))) {

                ResourceBundle resourceBundle = ResourceBundle.getBundle("utils/Nat", Locale.getDefault());
                loader.setResources(resourceBundle);
            }

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateHomeScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenNavigators.class.getResource("/ViewController/MainScreens/HomeScreen.fxml"));
            HomeScreenController controller = new HomeScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void navigateApptsScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenNavigators.class.getResource("/ViewController/MainScreens/ApptsScreen.fxml"));
            ApptsScreenController controller = new ApptsScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateCustInfoScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenNavigators.class.getResource("/ViewController/MainScreens/CustInfoScreen.fxml"));
            CustInfoScreenController controller = new CustInfoScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateReportsScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenNavigators.class.getResource("/ViewController/MainScreens/ReportsScreen.fxml"));
            ReportsScreenController controller = new ReportsScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
