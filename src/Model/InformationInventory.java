package Model;

import javafx.collections.ObservableList;
import static javafx.collections.FXCollections.observableArrayList;

public class InformationInventory {

    //Properties
    private ObservableList <User> allUsers;
    private ObservableList <Appointment> allAppointments;
    private ObservableList <Customer> allCustomers;

    //Constructor
    public InformationInventory() {
        allUsers        = observableArrayList();
        allAppointments = observableArrayList();
        allCustomers    = observableArrayList();
    }

    //Get Current User
    public User getCurrentUser() {
        return getAllUsers().get((getAllUsers().size() - 1));
    }

    //Populate List
    public void addUser(User userToAdd) {
        if (userToAdd != null) {
            this.allUsers.add(userToAdd);
        }
    }
    public void addAppointment(Appointment appointmentToAdd) {
        if (appointmentToAdd != null) {
            this.allAppointments.add(appointmentToAdd);
        }
    }
    public void addCustomer(Customer customerToAdd) {
        if (customerToAdd != null) {
            this.allCustomers.add(customerToAdd);
        }
    }

    //Update list item
    public void updateUser(User userToUpdate) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserId() == userToUpdate.getUserId()) {
                allUsers.set(i, userToUpdate);
                break;
            }
        }
    }
    public void updateAppointment(Appointment appointmentToUpdate) {
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).getAppointmentId() == appointmentToUpdate.getAppointmentId()) {
                allAppointments.set(i, appointmentToUpdate);
                break;
            }
        }
    }
    public void updateCustomer(Customer customerToUpdate) {
        for (int i = 0; i < allCustomers.size(); i++) {
            if (allCustomers.get(i).getCustomerId() == customerToUpdate.getCustomerId()) {
                allCustomers.set(i, customerToUpdate);
                break;
            }
        }
    }

    //Remove list item
    public void deleteUser(User userToDelete) {
        for (int i = 0; i < allUsers.size(); ++i) {
            if (allUsers.get(i).getUserId() == userToDelete.getUserId()) {
                allUsers.remove(i);
                break;
            }
            else {
                return;
            }
        }
    }
    public void deleteAppointment(Appointment appointmentToDelete) {
        for (int i = 0; i < allAppointments.size(); ++i) {
            if (allAppointments.get(i).getAppointmentId() == appointmentToDelete.getAppointmentId()) {
                allAppointments.remove(i);
                break;
            }
            else {
                return;
            }
        }
    }
    public void deleteCustomer(Customer customerToDelete) {
        for (int i = 0; i < allCustomers.size(); ++i) {
            if (allCustomers.get(i).getCustomerId() == customerToDelete.getCustomerId()) {
                allCustomers.remove(i);
                break;
            }
            else {
                return;
            }
        }
    }

    //Return all list items
    public ObservableList <User> getAllUsers() {
        return allUsers;
    }
    public ObservableList <Appointment> getAllAppointments() {
        return allAppointments;
    }
    public ObservableList <Customer> getAllCustomers() {
        return allCustomers;
    }

}