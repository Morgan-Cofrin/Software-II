package Model;

import java.sql.Timestamp;

public class Appointment extends LoginManager {

    //Properties
    private int appointmentId;
    private int customerId;
    private int userId;

    private String appointmentTitle;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String URL;
    private String start;
    private String end;

    private String userName;
    private String customerName;
    private String apptMonth;
    private int apptMonthNum;


    //Constructor
    public Appointment(int appointmentId, int customerId, int userId, String appointmentTitle,
                       String description, String type, String start, String end) {

        setAppointmentId(appointmentId);
        setCustomerId(customerId);
        setUserId(userId);
        setAppointmentTitle(appointmentTitle);
        setDescription(description);
        setLocation();
        setContact();
        setType(type);
        setURL();
        setStart(start);
        setEnd(end);

        setUserName("negative one");
        setCustomerName("negative one");
        setApptMonth("negative one");
        setApptMonthNum(-1);
    }

    //Overload for homeScreen
    public Appointment() {
        setCustomerName("N/A");
        setStart("0000-00-00 00:00");
        setEnd("0000-00-00 00:00");

    }


    //Setters
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation() {
        this.location = "not needed";
    }
    public void setContact() {
        this.contact = "not needed";
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setURL() {
        this.URL = "not needed";
    }
    public void setStart(String start) {
        this.start = start;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setApptMonth(String apptMonth) {
        this.apptMonth = apptMonth;
    }
    public void setApptMonthNum(int apptMonthNum) {
        this.apptMonthNum = apptMonthNum;
    }


    //Getters
    public int getAppointmentId() {
        return appointmentId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public int getUserId() {
        return userId;
    }
    public String getAppointmentTitle() {
        return appointmentTitle;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getContact() {
        return contact;
    }
    public String getType() {
        return type;
    }
    public String getURL() {
        return URL;
    }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }

    public String getUserName() {
        return userName;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getApptMonth() {
        return apptMonth;
    }
    public int getApptMonthNum() {
        return apptMonthNum;
    }

}