package ViewController.MainScreens;

import Model.Appointment;
import Model.InformationInventory;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import utils.ScreenNavigators;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {

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
    private TextField txt_CustomerName;
    @FXML
    private TextField txt_ApptTitle;
    @FXML
    private TextField txt_AppointmentDate;
    @FXML
    private TextField txt_AppointmentStartTime;
    @FXML
    private TextField txt_AppointmentEndTime;

    private ObservableList <Appointment> upcomingAppointments = FXCollections.observableArrayList();


    //Constructor
    public HomeScreenController(InformationInventory inv) {
        this.inv = inv;
        this.navigator = new ScreenNavigators(inv);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

        /*txt_CustomerName.setText(checkSoonestAppointment().getCustomerName());
        try {
            Date appointmentStartDate = dateFormat.parse(checkSoonestAppointment().getStart());
            txt_AppointmentDate.setText(String.valueOf(appointmentStartDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt_ApptTitle.setText(checkSoonestAppointment().getAppointmentTitle());
        txt_AppointmentStartTime.setText(checkSoonestAppointment().getStart().substring(11, 16));
        txt_AppointmentEndTime.setText(checkSoonestAppointment().getEnd().substring(11, 16));


        LocalDateTime localDateTime_Appt = LocalDateTime.parse(checkSoonestAppointment().getStart(), dateTimeFormatter);
        LocalDateTime localDateTime_plus15 = LocalDateTime.now().plusMinutes(15);

        if (!upcomingAppointments.isEmpty()) {
            if (localDateTime_Appt.isBefore(localDateTime_plus15)) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Attention");
                alert.setContentText("You have an appointment with " +
                                     checkSoonestAppointment().getCustomerName() +
                                     " at " + checkSoonestAppointment().getStart() + ".");
                alert.showAndWait();
            }
        }*/

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



    //go through appointments, find the ones that are after now(), put them in a list. From that list, find the one that has the soonest start time
    public Appointment checkSoonestAppointment() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime_now = LocalDateTime.now();
        Appointment soonestAppt = new Appointment();

        //Find all the appointments that are in the future
        for (int i = 0; i < inv.getAllAppointments().size(); i++) {

            LocalDateTime soonestAppt_StartTime = LocalDateTime.parse(inv.getAllAppointments().get(i).getStart(), dateTimeFormatter);

            if (soonestAppt_StartTime.isAfter(localDateTime_now)) {
                upcomingAppointments.add(inv.getAllAppointments().get(i));
            }
        }

        //Find the appointment with the soonest start time
        LocalDateTime earliestTime = LocalDateTime.parse(upcomingAppointments.get(0).getStart(), dateTimeFormatter);

        for (int i = 0; i < upcomingAppointments.size(); i++) {

            if (earliestTime.isAfter(LocalDateTime.parse(upcomingAppointments.get(i).getStart(), dateTimeFormatter))) {

                earliestTime = LocalDateTime.parse(upcomingAppointments.get(i).getStart(), dateTimeFormatter);
                soonestAppt = upcomingAppointments.get(i);
            }
        }

    return soonestAppt;

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
