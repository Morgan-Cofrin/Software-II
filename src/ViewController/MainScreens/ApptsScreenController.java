package ViewController.MainScreens;

import Model.Appointment;
import Model.InformationInventory;
import ViewController.InnerScreens.AddApptController;
import ViewController.InnerScreens.ModifyApptController;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApptsScreenController implements Initializable {

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
    private RadioButton radio_AllAppts;

    @FXML
    private RadioButton radio_ApptsByMonth;

    @FXML
    private RadioButton radio_ApptsByWeek;


    @FXML
    private Button btn_AddAppointment;
    @FXML
    private Button btn_ModifyAppointment;
    @FXML
    private Button btn_DeleteAppointment;


    @FXML
    private TableView <Appointment> appointmentTable;

    private ObservableList <Appointment> appointmentInventory = FXCollections.observableArrayList();
    private ObservableList <Appointment> appointmentByMonth = FXCollections.observableArrayList();
    private ObservableList <Appointment> appointmentByWeek = FXCollections.observableArrayList();




    public ApptsScreenController(InformationInventory inv) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        final ToggleGroup group = new ToggleGroup();
        radio_AllAppts.setToggleGroup(group);
        radio_ApptsByMonth.setToggleGroup(group);
        radio_ApptsByWeek.setToggleGroup(group);
        radio_AllAppts.setSelected(true);


        generateAppointmentsTable();

        lblUsername.setText("Welcome, " + inv.getCurrentUser().getUserName());
        initClock();
    }

    //Radio Buttons for filtering
    @FXML
    void allApptsSelected(MouseEvent event) {

        generateAppointmentsTable();
    }

    @FXML
    void apptsByMonthSelected(MouseEvent event) throws ParseException {

        appointmentByMonth.clear();

        for (int i = 0; i < inv.getAllAppointments().size(); i++) {

            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(inv.getAllAppointments().get(i).getStart());
            LocalDateTime localDateTime = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (localDateTime.isAfter(LocalDateTime.now()) && localDateTime.isBefore(LocalDateTime.now().plusDays(30))) {

                appointmentByMonth.add(inv.getAllAppointments().get(i));
            }
        }

        appointmentTable.setItems(appointmentByMonth);
        appointmentTable.refresh();

    }

    @FXML
    void apptsByWeekSelected(MouseEvent event) throws ParseException {

        appointmentByWeek.clear();

        for (int i = 0; i < inv.getAllAppointments().size(); i++) {

            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(inv.getAllAppointments().get(i).getStart());
            LocalDateTime localDateTime = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (localDateTime.isAfter(LocalDateTime.now()) && localDateTime.isBefore(LocalDateTime.now().plusDays(7))) {

                appointmentByWeek.add(inv.getAllAppointments().get(i));
            }
        }

        appointmentTable.setItems(appointmentByWeek);
        appointmentTable.refresh();
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
    void gotoAddAppointmentScreen(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/InnerScreens/AddAppt.fxml"));
            AddApptController controller = new AddApptController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void gotoModifyAppointmentScreen(MouseEvent event) {

        try {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Select an appointment to modify");
                alert.showAndWait();
            }

            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/InnerScreens/ModifyAppt.fxml"));
                ModifyApptController controller = new ModifyApptController(inv, selectedAppointment);

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
    void deleteAppointment(MouseEvent event) {

        Appointment removeAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (removeAppointment == null) {
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
            deleteAlert.setTitle("Attention");
            deleteAlert.setHeaderText(null);
            deleteAlert.setContentText("Select an appointment to delete");
            deleteAlert.showAndWait();
        }
        else {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Confirm deletion");
            confirmationAlert.setContentText("Are you sure you want to delete " + removeAppointment.getAppointmentTitle() + "?");

            Optional <ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {

                try {
                    String sqlStatement = "DELETE FROM appointment WHERE appointmentId = ?;";

                    PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                    ps.setInt(1, removeAppointment.getAppointmentId());
                    ps.executeUpdate();


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                inv.deleteAppointment(removeAppointment);
                inv.getAllAppointments().remove(removeAppointment);
                appointmentInventory.setAll(inv.getAllAppointments());
                appointmentTable.refresh();
            }
        }
    }



    private void generateAppointmentsTable() {

        appointmentInventory.setAll(inv.getAllAppointments());
        appointmentTable.setItems(appointmentInventory);
        appointmentTable.refresh();
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

