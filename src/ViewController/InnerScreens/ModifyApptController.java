package ViewController.InnerScreens;

import Model.Appointment;
import Model.Customer;
import Model.InformationInventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.DBConnection;
import utils.ScreenNavigators;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static java.time.ZoneId.systemDefault;
import static java.time.ZoneOffset.UTC;

public class ModifyApptController implements Initializable {

    //Buttons, lists, fields and tables inside

    InformationInventory inv;
    ScreenNavigators navigator = null;
    Appointment selectedAppointment;
    private static final String LAST_INSERT_ID_SQL = "select last_insert_id() as ID";

    @FXML
    private TextField textFieldApptTitle;
    @FXML
    private TextField textFieldCustomerName;
    @FXML
    private TextField textFieldApptDescription;
    @FXML
    private ChoiceBox<String> choice_ApptType;
    @FXML
    private DatePicker datePickApptDate;
    @FXML
    private TextField textFieldStartTime;
    @FXML
    private TextField textFieldEndTime;

    @FXML
    private Button cancelButton1;
    @FXML
    private Button btn_SelectCustomer;
    @FXML
    private Button cancelButton2;
    @FXML
    private Button btnSaveAppt;

    @FXML
    private TabPane tabPane_NewAppointment;
    @FXML
    private Tab tab_CustomerInfo;
    @FXML
    private Tab tab_AppointmentInfo;

    @FXML
    private TableView<Customer> tbl_CustomerTable;
    private ObservableList <Customer> customerInventory = FXCollections.observableArrayList();
    Customer selectedCustomer;


    public ModifyApptController(InformationInventory inv, Appointment selectedAppointment) {
        this.inv       = inv;
        this.navigator = new ScreenNavigators(inv);
        this.selectedAppointment = selectedAppointment;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateCustomerTable();
        choice_ApptType.getItems().add("Status Update");
        choice_ApptType.getItems().add("Scrum");
        choice_ApptType.getItems().add("Presentation");

        //Pull customer name from customerId
        int selectedCustomerId = selectedAppointment.getCustomerId();
        //Remember, lists start at 0 and Id's start at 1. Have to run through the inv and .get()-1
        String selectedCustomerName = inv.getAllCustomers().get(selectedCustomerId-1).getCustomerName();

        selectedCustomer = inv.getAllCustomers().get(selectedCustomerId-1);

        textFieldApptTitle.setText(selectedAppointment.getAppointmentTitle());
        textFieldCustomerName.setText(selectedCustomerName);
        textFieldApptDescription.setText(selectedAppointment.getDescription());
        choice_ApptType.setValue(selectedAppointment.getType());
        datePickApptDate.setValue(LocalDate.parse(selectedAppointment.getStart().substring(0, 10)));
        textFieldStartTime.setText(selectedAppointment.getStart().substring(11, 16));
        textFieldEndTime.setText(selectedAppointment.getEnd().substring(11, 16));
    }


    @FXML
    void chooseSelectedCustomer(MouseEvent event) {
        selectedCustomer = tbl_CustomerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Select a customer schedule an appointment with");
            alert.showAndWait();
        }

        assert selectedCustomer != null;
        textFieldCustomerName.setText(selectedCustomer.getCustomerName());

        Alert customerSelectedAlert = new Alert(Alert.AlertType.CONFIRMATION);
        customerSelectedAlert.setTitle("Confirmation");
        customerSelectedAlert.setContentText("Schedule with " + selectedCustomer.getCustomerName() + "?");
        customerSelectedAlert.showAndWait();

        Alert gotoApptInfo = new Alert(Alert.AlertType.INFORMATION);
        gotoApptInfo.setContentText("Please proceed to Appointment Information");
        gotoApptInfo.showAndWait();
    }


    @FXML
    void updateAppointment(MouseEvent event) {

    try {
        //Check if any fields are empty
        if (textFieldApptTitle.getText().isEmpty() ||
            textFieldCustomerName.getText().isEmpty() ||
            textFieldApptDescription.getText().isEmpty() ||
            choice_ApptType.getValue().isEmpty() ||
            datePickApptDate.getValue() == null ||
            textFieldStartTime.getText().isEmpty() ||
            textFieldEndTime.getText().isEmpty() ) {
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill all fields. Enter N/A if not applicable.");
                alert.showAndWait();
            }
        }
        //Check if the appointment is within business hours and checks starts sooner than ending, function beneath
        else if (convertTime(textFieldStartTime.getText()) < 900 || convertTime(textFieldEndTime.getText()) > 1700 ||
                 convertTime(textFieldStartTime.getText()) > convertTime(textFieldEndTime.getText())) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Appointments can only be scheduled between 9:00 AM and 5:00 PM");
            alert.showAndWait();
        }
            //Check if the new appointment start and end times overlap with other appointments
        else if (isOverlapping() ) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("An appointment is already scheduled during that time.");
            alert.showAndWait();
        }
        else {
            String startDateAndTime = datePickApptDate.getValue() + " " + textFieldStartTime.getText();
            String endDateAndTime = datePickApptDate.getValue() + " " + textFieldEndTime.getText();

            selectedAppointment.setAppointmentTitle(textFieldApptTitle.getText());
            selectedAppointment.setCustomerId(selectedCustomer.getCustomerId());
            selectedAppointment.setDescription(textFieldApptDescription.getText());
            selectedAppointment.setStart(startDateAndTime);
            selectedAppointment.setEnd(endDateAndTime);

            try {

                String sqlStatement = "UPDATE appointment SET customerId = ?, title = ?, description = ?, start = ?, end = ? WHERE appointmentId = ?;";
                PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqlStatement);

                ps.setInt(1, selectedAppointment.getCustomerId());
                ps.setString(2, textFieldApptTitle.getText());
                ps.setString(3, textFieldApptDescription.getText());
                ps.setTimestamp(4, convertLocalToUTC(startDateAndTime));
                ps.setTimestamp(5, convertLocalToUTC(endDateAndTime));
                ps.setInt(6, selectedAppointment.getAppointmentId());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Insert appointment to inv after the Id update
            inv.updateAppointment(selectedAppointment);

            navigator.navigateApptsScreen(event);
        }
            //Catch if times have characters in them
        } catch (NumberFormatException | ParseException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Check appointment times, times cannot contain letters.");
            alert.showAndWait();
        }
    }


    @FXML
    void returnScreen(MouseEvent event) {
        navigator.navigateApptsScreen(event);
    }


    private void generateCustomerTable() {
        customerInventory.setAll(inv.getAllCustomers());
        tbl_CustomerTable.setItems(customerInventory);
        tbl_CustomerTable.refresh();
    }

    private int convertTime(String s) {
        String formattedTime = "";
        char[] characterArray;
        characterArray = s.toCharArray();

        for (int i = 0; i < characterArray.length; i++) {
            if(Character.isDigit(characterArray[i]))
                formattedTime += characterArray[i];
        }
        return Integer.parseInt(formattedTime);
    }


    private boolean isOverlapping() throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < inv.getAllAppointments().size(); i++) {
            LocalDateTime startDT = LocalDateTime.parse(inv.getAllAppointments().get(i).getStart(), formatter);
            LocalDateTime endDT = LocalDateTime.parse(inv.getAllAppointments().get(i).getEnd(), formatter);

            LocalDateTime newApptStartDT = LocalDateTime.parse(datePickApptDate.getValue() + " " +
                                                          textFieldStartTime.getText(), formatter);
            LocalDateTime newApptEndDT = LocalDateTime.parse(datePickApptDate.getValue() + " " +
                                                          textFieldEndTime.getText(), formatter);

            if(((newApptStartDT.isAfter(startDT) || newApptStartDT.isEqual(startDT)) && (newApptStartDT.isBefore(endDT) || newApptStartDT.isEqual(endDT))) ||
               (newApptEndDT.isAfter(startDT) || newApptEndDT.isEqual(startDT)) && (newApptEndDT.isBefore(endDT) || newApptEndDT.isEqual(endDT)) ) {
                return true;
            }
        }
        return false;
    }


    //TODO cleanup
    public static Timestamp convertLocalToUTC(String inputString) {

        //System.out.println(inputString + "  inputString");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime localDateTime_system = LocalDateTime.parse(inputString, formatter);
        //System.out.println(localDateTime_system + "  localDateTime_system");

        ZonedDateTime zonedDateTime_system = ZonedDateTime.of(localDateTime_system.toLocalDate(), localDateTime_system.toLocalTime(), systemDefault());
        //System.out.println(zonedDateTime_system + "  zonedDateTime_system");

        ZonedDateTime zonedDateTime_UTC = zonedDateTime_system.withZoneSameInstant(UTC);
        //System.out.println(zonedDateTime_UTC + "  zonedDateTime_UTC");

        LocalDateTime localDateTime_UTC = zonedDateTime_UTC.toLocalDateTime();
        //System.out.println(localDateTime_UTC + "  localDateTime_UTC");

        Timestamp timestamp_UTC_forDB = Timestamp.valueOf(localDateTime_UTC);
        //System.out.println(timestamp_UTC_forDB + "  timestamp_UTC_forDB");


        return timestamp_UTC_forDB;

    }


}
