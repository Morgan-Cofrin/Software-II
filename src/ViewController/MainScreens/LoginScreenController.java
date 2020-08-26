package ViewController.MainScreens;

import Model.InformationInventory;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.DBConnection;
import utils.ScreenNavigators;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {


    //Buttons, lists, fields and tables inside

    ResourceBundle resourceBundle;

    InformationInventory inv;
    ScreenNavigators navigator = null;
    private static final String LAST_INSERT_ID_SQL = "SELECT LAST_INSERT_ID() AS ID";

    @FXML
    private Label lblHeaderLabel;
    @FXML
    private TextField txtFieldUsername;
    @FXML
    private PasswordField txtFieldUserPassword;
    @FXML
    private Button btnUserLogin;
    @FXML
    private Button btbSkipLogin;


    //Constructor
    public LoginScreenController(InformationInventory inv) {
        this.inv = inv;
        this.navigator=new ScreenNavigators(inv);
    }


    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;
        System.out.println(Locale.getDefault());

        lblHeaderLabel.setText(resourceBundle.getString("Welcome"));
        txtFieldUsername.setPromptText(resourceBundle.getString("Username"));
        txtFieldUserPassword.setPromptText(resourceBundle.getString("Password"));
        btnUserLogin.setText(resourceBundle.getString("Login"));


    }

    //Check if username and password match, if so, enter application. If not, error popup
    @FXML
    void authenticateUser(MouseEvent event) throws SQLException, IOException {

        if (txtFieldUsername.getText().isEmpty() || txtFieldUserPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resourceBundle.getString("Warning"));
            alert.setHeaderText(null);
            alert.setContentText(resourceBundle.getString("key_EmptyFieldWarning"));
            alert.showAndWait();

        } //check if empty
        else {
            //check username
            if (!txtFieldUsername.getText().equals("test")) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resourceBundle.getString("Warning"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("key_UsernameWarning"));
                alert.showAndWait();

            }
            //check password
            else if (!txtFieldUserPassword.getText().equals("test")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resourceBundle.getString("Warning"));
                alert.setHeaderText(null);
                alert.setContentText(resourceBundle.getString("key_PasswordWarning"));
                alert.showAndWait();

            }
            //Add user object to inv, execute sql, goto Home
            else {

                User currentUser = new User(-1, txtFieldUsername.getText(), txtFieldUserPassword.getText(), 1);

                PreparedStatement ps = null;
                String sqlInsertUser = "INSERT INTO user(userName, password, active, createDate, createdBy, lastUpdateBy) \n" +
                                       "VALUES (?, ?, ?, ?, ?, ?)";

                ps = DBConnection.startConnection().prepareStatement(sqlInsertUser);

                ps.setString(1, currentUser.getUserName());
                ps.setString(2, currentUser.getUserName());
                ps.setInt(3, currentUser.getActive());
                ps.setTimestamp(4, currentUser.getCreateDate());
                ps.setString(5, currentUser.getCreatedBy());
                ps.setString(6, currentUser.getLastUpdateBy());

                ps.executeUpdate();

                //change userId
                currentUser.setUserId(getLastId());

                inv.addUser(currentUser);


                //FileWriter: records timestamps for user login's in .txt file
                FileWriter fileWriter = new FileWriter("userlogs", true);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                printWriter.println(currentUser.getUserName() + " " + currentUser.getCreateDate());

                printWriter.close();
                System.out.println("File written");




                navigator.navigateHomeScreen(event);

            }
        }
    }


    //dev button, go to home without entering username and password every time
    @FXML
    void skipAuthentication(MouseEvent event) {

        User currentUserSkip = new User(1,"userNameSkip", "passwordSkip", 1);
        inv.addUser(currentUserSkip);
        navigator.navigateHomeScreen(event);
    }



    public int getLastId() throws SQLException {
        PreparedStatement ps = DBConnection.startConnection().prepareStatement(LAST_INSERT_ID_SQL);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt("ID");
    }


}
