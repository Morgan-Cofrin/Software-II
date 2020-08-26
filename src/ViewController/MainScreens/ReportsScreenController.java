package ViewController.MainScreens;

import Model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import utils.ScreenNavigators;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static jdk.nashorn.internal.objects.Global.print;

public class ReportsScreenController implements Initializable {

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


    //Report 1
    @FXML
    private TableView <Appointment> tbl_ReportNumber1;
    private ObservableList <Appointment> report1List = FXCollections.observableArrayList();
    @FXML
    private TextArea txtArea_ApptsBreakdown;

    private String[] MONTH_NAMES = new String[] {"January", "February", "March", "April", "May", "June",
                                                 "July", "August", "September", "October", "November", "December"};
    //Array 1-12 are months, index 1 is Status Update, index 2 is Presentation, index 3 is Scrum
    private int[][] arr_ApptByMonth = new int[12][3];


    //Report 2
    @FXML
    private TableView<Appointment> tbl_ReportNumber2;
    private ObservableList <Appointment> report2List = FXCollections.observableArrayList();
    private ObservableList <Appointment> report2FilteredList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> choice_UserList;


    //Report 3
    @FXML
    private TableView<Customer> tbl_ReportNumber3;
    private ObservableList <Customer> report3List = FXCollections.observableArrayList();
    private ObservableList <Customer> report3FilteredList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> choice_CountryList;




    //Constructor
    public ReportsScreenController(InformationInventory inv) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsername.setText("Welcome, " + inv.getCurrentUser().getUserName());
        initClock();

        //------//

        //Beginning- Report 1: display number of appointment types for each month
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (int i = 0; i < inv.getAllAppointments().size(); i++) {
                Appointment currentAppointment = inv.getAllAppointments().get(i);

                //Date string to Date object
                Date appointmentStartDate = dateFormat.parse(inv.getAllAppointments().get(i).getStart());
                //Pull month from date object
                int intDate = Integer.parseInt(String.valueOf(appointmentStartDate.getMonth()));

                //months start at 0 because of course they do
                //assign each appointment a month name, and a month number
                //also put the appointment in proper spot in the 2D array, function on bottom of page
                if (intDate == 0) {
                    currentAppointment.setApptMonth("Jan");
                    currentAppointment.setApptMonthNum(0);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 1){
                    currentAppointment.setApptMonth("Feb");
                    currentAppointment.setApptMonthNum(1);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 2){
                    currentAppointment.setApptMonth("Mar");
                    currentAppointment.setApptMonthNum(2);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 3){
                    currentAppointment.setApptMonth("Apr");
                    currentAppointment.setApptMonthNum(3);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 4){
                    currentAppointment.setApptMonth("May");
                    currentAppointment.setApptMonthNum(4);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 5){
                    currentAppointment.setApptMonth("June");
                    currentAppointment.setApptMonthNum(5);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 6){
                    currentAppointment.setApptMonth("Jul");
                    currentAppointment.setApptMonthNum(6);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 7){
                    currentAppointment.setApptMonth("Aug");
                    currentAppointment.setApptMonthNum(7);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 8){
                    currentAppointment.setApptMonth("Sep");
                    currentAppointment.setApptMonthNum(8);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 9){
                    currentAppointment.setApptMonth("Oct");
                    currentAppointment.setApptMonthNum(9);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 10){
                    currentAppointment.setApptMonth("Nov");
                    currentAppointment.setApptMonthNum(10);
                    checkAppointmentType(currentAppointment);
                }
                if (intDate == 11){
                    currentAppointment.setApptMonth("Dec");
                    currentAppointment.setApptMonthNum(11);
                    checkAppointmentType(currentAppointment);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        generateReport1Table();

        //Fill text area
        for (int i = 0; i < arr_ApptByMonth.length; i++) {
            txtArea_ApptsBreakdown.appendText(MONTH_NAMES[i] + ": " + "   ");
            txtArea_ApptsBreakdown.appendText("Status Update: " + (arr_ApptByMonth[i][0]) + "   " +
                                              "Presentation: " + (arr_ApptByMonth[i][1]) + "   " +
                                              "Scrum: " + (arr_ApptByMonth[i][2]) + "\n");
        }
        //End- Report 1: display number of appointment types for each month


        //------//


        //Beginning Report 2: display schedule for each consultant
        generateReport2Table();

        //Fill choice box
        choice_UserList.getItems().add("All Users");
        for (int i = 0; i < inv.getAllUsers().size(); i++) {
            choice_UserList.getItems().add(inv.getAllUsers().get(i).getUserName());
        }

            // <----- LAMBDA EXPRESSION -----> //
        //A lambda expression is used to filter a TableView using a selected UserName in a Choice Box
        choice_UserList.setOnAction(event -> {
            String choiceBoxSelection = choice_UserList.getValue();
            if (choiceBoxSelection.equals("All Users")){
                generateReport2Table();
            }
            else{
                report2FilteredList.clear();
                for (int i = 0; i < inv.getAllAppointments().size(); i++) {
                    if (inv.getAllAppointments().get(i).getUserName().equals(choiceBoxSelection)) {
                        report2FilteredList.add(inv.getAllAppointments().get(i));
                    }
                    tbl_ReportNumber2.setItems(report2FilteredList);
                    tbl_ReportNumber2.refresh();
                }
            }
        });
        //End - Report 2: display schedule for each consultant


        //------//
        

        //Beginning Report 3: display all customers' countries, filtered by country
        generateReport3Table();

        //Fill choice box
        choice_CountryList.getItems().add("All Countries");
        for (int i = 0; i < inv.getAllCustomers().size(); i++) {
            choice_CountryList.getItems().add(inv.getAllCustomers().get(i).getCustomerCountry());
        }

            // <----- LAMBDA EXPRESSION -----> //
        //A lambda expression is used to filter a TableView using a selected UserName in a Choice Box
        choice_CountryList.setOnAction(event -> {
            String choiceBoxSelection = choice_CountryList.getValue();
            if (choiceBoxSelection.equals("All Countries")){
                generateReport3Table();
            }
            else{
                report3FilteredList.clear();
                for (int i = 0; i < inv.getAllCustomers().size(); i++) {
                    if (inv.getAllCustomers().get(i).getCustomerCountry().equals(choiceBoxSelection)) {
                        report3FilteredList.add(inv.getAllCustomers().get(i));
                    }
                    tbl_ReportNumber3.setItems(report3FilteredList);
                    tbl_ReportNumber3.refresh();
                }
            }
        });
        //End Report 3: display all customers' countries, filtered by country


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



    private void checkAppointmentType(Appointment currentAppointment) {
        if (currentAppointment.getType().equals("Status Update")) {
            arr_ApptByMonth[currentAppointment.getApptMonthNum()][0] += 1;
        }
        if (currentAppointment.getType().equals("Presentation")) {
            arr_ApptByMonth[currentAppointment.getApptMonthNum()][1] += 1;
        }
        if (currentAppointment.getType().equals("Scrum")) {
            arr_ApptByMonth[currentAppointment.getApptMonthNum()][2] += 1;
        }
    }


    private void generateReport1Table() {
        report1List.setAll(inv.getAllAppointments());
        tbl_ReportNumber1.setItems(report1List);
        tbl_ReportNumber1.refresh();
    }

    private void generateReport2Table() {
        report2List.setAll(inv.getAllAppointments());
        tbl_ReportNumber2.setItems(report2List);
        tbl_ReportNumber2.refresh();
    }

    private void generateReport3Table() {
        report3List.setAll(inv.getAllCustomers());
        tbl_ReportNumber3.setItems(report3List);
        tbl_ReportNumber3.refresh();
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
