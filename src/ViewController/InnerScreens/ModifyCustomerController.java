package ViewController.InnerScreens;

import Model.Customer;
import Model.InformationInventory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.DBConnection;
import utils.ScreenNavigators;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    //Buttons, lists, fields and tables inside

    InformationInventory inv;
    ScreenNavigators navigator = null;
    Customer selectedCustomer;

    @FXML
    private Label lblCustomerIdToChange;
    @FXML
    private TextField textFieldCustomerName;
    @FXML
    private TextField textFieldCustomerPhone;
    @FXML
    private TextField textFieldCustomerAddress1;
    @FXML
    private TextField textFieldCustomerAddress2;
    @FXML
    private TextField textFieldCustomerPostalCode;
    @FXML
    private TextField textFieldCustomerCity;
    @FXML
    private TextField textFieldCustomerCountry;

    @FXML
    private Button btnUpdateCustomer;
    @FXML
    private Button cancelButton;


    public ModifyCustomerController(InformationInventory inv, Customer selectedCustomer) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
        this.selectedCustomer = selectedCustomer;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        textFieldCustomerName.setText(selectedCustomer.getCustomerName());
        textFieldCustomerPhone.setText(selectedCustomer.getCustomerPhoneNum());
        textFieldCustomerAddress1.setText(selectedCustomer.getCustomerAddressLn1());
        textFieldCustomerAddress2.setText(selectedCustomer.getCustomerAddressLn2());
        textFieldCustomerPostalCode.setText(selectedCustomer.getCustomerPhoneNum());
        textFieldCustomerCity.setText(selectedCustomer.getCustomerCity());
        textFieldCustomerCountry.setText(selectedCustomer.getCustomerCountry());

    }


    @FXML
    void updateCustomer(MouseEvent event) {
        //Check if any fields are empty
        if (textFieldCustomerName.getText().isEmpty() ||
            textFieldCustomerPhone.getText().isEmpty() ||
            textFieldCustomerAddress1.getText().isEmpty() ||
            textFieldCustomerAddress2.getText().isEmpty() ||
            textFieldCustomerPostalCode.getText().isEmpty() ||
            textFieldCustomerCity.getText().isEmpty() ||
            textFieldCustomerCountry.getText().isEmpty() )
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill all fields. Enter N/A if not applicable.");
                alert.showAndWait();
            }
        else {

            selectedCustomer.setCustomerName(textFieldCustomerName.getText());
            selectedCustomer.setCustomerPhoneNum(textFieldCustomerPhone.getText());
            selectedCustomer.setCustomerAddressLn1(textFieldCustomerAddress1.getText());
            selectedCustomer.setCustomerAddressLn2(textFieldCustomerAddress2.getText());
            selectedCustomer.setCustomerPostalCode(textFieldCustomerPostalCode.getText());
            selectedCustomer.setCustomerCity(textFieldCustomerCity.getText());
            selectedCustomer.setCustomerCountry(textFieldCustomerCountry.getText());

            //Update Country
            try {
                String sqlStatement = "UPDATE country SET country = ? WHERE countryId = ?;";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerCountry.getText());
                ps.setInt(2, selectedCustomer.getCountryId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Update City
            try {
                String sqlStatement = "UPDATE city SET city = ? WHERE cityId = ?;";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerCity.getText());
                ps.setInt(2, selectedCustomer.getCityId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Update Address
            try {
                String sqlStatement = "UPDATE address SET address = ?, address2 = ?, postalCode = ?, phone = ? WHERE addressId = ?;";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerAddress1.getText());
                ps.setString(2, textFieldCustomerAddress2.getText());
                ps.setString(3, textFieldCustomerPostalCode.getText());
                ps.setString(4, textFieldCustomerPhone.getText());
                ps.setInt(5, selectedCustomer.getAddressId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Update Customer
            try {
                String sqlStatement = "UPDATE customer SET customerName = ? WHERE customerId = ?;";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerName.getText());
                ps.setInt(2, selectedCustomer.getCustomerId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


            inv.updateCustomer(selectedCustomer);
            navigator.navigateCustInfoScreen(event);

        }
    }


    @FXML
    void returnScreen(MouseEvent event) {
        navigator.navigateCustInfoScreen(event);
    }


}
