package ViewController.InnerScreens;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.DBConnection;
import utils.ScreenNavigators;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    //Buttons, lists, fields and tables inside

    InformationInventory inv;
    ScreenNavigators navigator = null;
    public static String LAST_INSERT_ID_SQL = "select last_insert_id() as ID";

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
    private Button btnSaveNewCustomer;
    @FXML
    private Button cancelButton;


    public AddCustomerController(InformationInventory inv) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void saveNewCustomer(MouseEvent event) {

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

            //Add new customer
            Customer newCustomer = new Customer(

                    -1, -1, -1, -1,
                    textFieldCustomerName.getText(), 1, textFieldCustomerAddress1.getText(),
                    textFieldCustomerAddress2.getText(), textFieldCustomerPostalCode.getText(),
                    textFieldCustomerPhone.getText(), textFieldCustomerCity.getText(), textFieldCustomerCountry.getText()
            );

        //Remember to insert rows in the right order. Inner to outer, opposite of deletion
            //Add Country in SQL
            try {
                String sqlStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy)\n" +
                                      "VALUES(?, ?, ?, ?);";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerCountry.getText());
                ps.setTimestamp(2, newCustomer.getCreateDate());
                ps.setString(3, inv.getCurrentUser().getUserName());
                ps.setString(4, inv.getCurrentUser().getUserName());
                ps.executeUpdate();

                newCustomer.setCountryId(getLastId());

            }catch (SQLException e) {
                e.printStackTrace();
            }

            //Add city in SQL
            try {
                String sqlStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdateBy)\n" +
                                      "VALUES (?, ?, ?, ?, ?);";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerCity.getText());
                ps.setInt(2, newCustomer.getCountryId());
                ps.setTimestamp(3, newCustomer.getCreateDate());
                ps.setString(4, inv.getCurrentUser().getUserName());
                ps.setString(5, inv.getCurrentUser().getUserName());
                ps.executeUpdate();

                newCustomer.setCityId(getLastId());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Add address in SQL
            try {
                String sqlStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerAddress1.getText());
                ps.setString(2, textFieldCustomerAddress2.getText());
                ps.setInt(3, newCustomer.getCityId());
                ps.setString(4, textFieldCustomerPostalCode.getText());
                ps.setString(5, textFieldCustomerPhone.getText());
                ps.setTimestamp(6, newCustomer.getCreateDate());
                ps.setString(7, inv.getCurrentUser().getUserName());
                ps.setString(8, inv.getCurrentUser().getUserName());
                ps.executeUpdate();

                newCustomer.setAddressId(getLastId());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Add customer in SQL
            try {
                String sqlStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy)\n" +
                                      "VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setString(1, textFieldCustomerName.getText());
                ps.setInt(2, newCustomer.getAddressId());
                ps.setInt(3, newCustomer.getActive());
                ps.setTimestamp(4, newCustomer.getCreateDate());
                ps.setString(5, inv.getCurrentUser().getUserName());
                ps.setString(6, inv.getCurrentUser().getUserName());
                ps.executeUpdate();

                newCustomer.setCustomerId(getLastId());

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Add customer to the lists after Id updates
            inv.addCustomer(newCustomer);

            navigator.navigateCustInfoScreen(event);

        }

    }


    @FXML
    void returnScreen(MouseEvent event) {
        navigator.navigateCustInfoScreen(event);
    }


    public int getLastId() throws SQLException {

        PreparedStatement ps = DBConnection.startConnection().prepareStatement(LAST_INSERT_ID_SQL);

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("ID");
    }


}