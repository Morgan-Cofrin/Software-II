package ViewController.MainScreens;

import Model.Customer;
import Model.InformationInventory;
import ViewController.InnerScreens.AddCustomerController;
import ViewController.InnerScreens.ModifyCustomerController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.DBConnection;
import utils.ScreenNavigators;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustInfoScreenController implements Initializable {

    //Buttons, lists, fields and tables inside

    InformationInventory inv;
    ScreenNavigators navigator = null;

    @FXML
    private Button btnNavHomeScreen;
    @FXML
    private Button btnNavApptsScreen;
    @FXML
    private Button btnNavCustInfoScreen;
    @FXML
    private Button btnNavReportsScreen;
    @FXML
    private Button btnUserLogout;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblDateAndTime;

    @FXML
    private Button btn_AddAppointment;
    @FXML
    private Button btn_ModifyAppointment;
    @FXML
    private Button btn_DeleteAppointment;

    @FXML
    private TableView <Customer> customerTable;



    private ObservableList <Customer> customerInventory = FXCollections.observableArrayList();



    //Constructor
    public CustInfoScreenController(InformationInventory inv) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateCustomerTable();

        lblUsername.setText("Welcome, " + inv.getCurrentUser().getUserName());
        initClock();

    }


    //Sidebar navigators
    @FXML
    void gotoHomeScreen(MouseEvent event) {

        navigator.navigateHomeScreen(event);

    }
    @FXML
    void gotoApptsScreen(MouseEvent event) {

        navigator.navigateApptsScreen(event);

    }
    @FXML
    void gotoCustInfoScreen(MouseEvent event) {

        navigator.navigateCustInfoScreen(event);

    }
    @FXML
    void gotoReportsScreen(MouseEvent event) {

        navigator.navigateReportsScreen(event);

    }
    @FXML
    void gotoUserLoginScreen(MouseEvent event) {

        navigator.navigateLoginScreen(event);

    }


    @FXML
    void gotoAddCustomerScreen(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/InnerScreens/AddCustomer.fxml"));
            AddCustomerController controller = new AddCustomerController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    @FXML
    void gotoModifyCustomerScreen(MouseEvent event) {

        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Select a customer to modify");
                alert.showAndWait();
            }
            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/InnerScreens/ModifyCustomer.fxml"));
                ModifyCustomerController controller = new ModifyCustomerController(inv, selectedCustomer);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void deleteCustomer(MouseEvent event) throws SQLException {

        Customer removeCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (removeCustomer == null) {
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
            deleteAlert.setTitle("Attention");
            deleteAlert.setHeaderText(null);
            deleteAlert.setContentText("Select a customer to delete");
            deleteAlert.showAndWait();
        }
        else {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Confirm deletion");
            confirmationAlert.setContentText("Are you sure you want to delete " + removeCustomer.getCustomerName() + "?");

            Optional <ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try{
                    //Remember to delete the rows in the right order. Outer to inner, opposite of inserting

                    //Delete Customer
                    String sqlStatement = "DELETE FROM customer WHERE customerId = ?;";
                    PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                    ps.setInt(1, removeCustomer.getCustomerId());
                    ps.executeUpdate();

                    //Delete Address
                    sqlStatement = "DELETE FROM address WHERE addressId = ?;";
                    ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                    ps.setInt(1, removeCustomer.getAddressId());
                    ps.executeUpdate();

                    //Delete City
                    sqlStatement = "DELETE FROM city WHERE cityId = ?;";
                    ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                    ps.setInt(1, removeCustomer.getCityId());
                    ps.executeUpdate();

                    //Delete Country
                    sqlStatement = "DELETE FROM country WHERE countryId = ?;";
                    ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                    ps.setInt(1, removeCustomer.getCountryId());
                    ps.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                inv.deleteCustomer(removeCustomer);
                inv.getAllCustomers().remove(removeCustomer);
                customerInventory.setAll(inv.getAllCustomers());
                customerTable.refresh();

            }
        }
    }



    private void generateCustomerTable() {
        customerInventory.setAll(inv.getAllCustomers());
        customerTable.setItems(customerInventory);
        customerTable.refresh();
    }

    public void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            lblDateAndTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


}
