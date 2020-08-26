package utils;

import Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

public class SQL_ListFillers {

    private static String sqlStatement;
    private static ResultSet resultSet;


    public static void populateUserList(InformationInventory inv) throws SQLException {

        sqlStatement = "SELECT * FROM user;";

        QueryManager.makeQuery(sqlStatement);
        resultSet = QueryManager.getResult();

        while (resultSet.next()) {
            int userId = resultSet.getInt("userId");
            String userName = resultSet.getString("userName");
            String password = resultSet.getString("password");
            int active = resultSet.getInt("active");

            User newUser = new User(userId, userName, password, active);
            inv.addUser(newUser);
        }

    }


    public static void populateAppointmentList(InformationInventory inv) throws SQLException {

        sqlStatement = "SELECT *, customer.customerName FROM appointment INNER JOIN customer USING (customerId)";

        QueryManager.makeQuery(sqlStatement);
        resultSet = QueryManager.getResult();

        while (resultSet.next()) {

            int appointmentId = resultSet.getInt("appointmentId");
            int customerId = resultSet.getInt("customerId");
            int userId = resultSet.getInt("userId");
            String appointmentTitle = resultSet.getString("title");
            String description = resultSet.getString("description");
            String type = resultSet.getString("type");

            //Pull from DB in UTC, into LocalDateTime UTC, into ZonedDateTime UTC, into ZonedDateTime system, into LocalDateTime system, into string
            String start = convertUTCToLocal(resultSet.getTimestamp("start"));
            String end = convertUTCToLocal(resultSet.getTimestamp("end"));

            String customerName = resultSet.getString("customerName");

            Appointment newAppointment = new Appointment(appointmentId, customerId, userId, appointmentTitle, description, type, start, end);
            newAppointment.setUserName(inv.getCurrentUser().getUserName());
            newAppointment.setCustomerName(customerName);
            inv.addAppointment(newAppointment);
        }

    }


    public static void populateCustomerList(InformationInventory inv) throws SQLException {

        sqlStatement = "select customer.customerId, address.addressId, city.cityId, country.countryId, customer.customerName, \n" +
                       "customer.active, address.address, address.address2, address.postalCode, address.phone, city.city, country.country\n" +
                       "FROM customer\n" +
                       "INNER JOIN address USING (addressId)\n" +
                       "INNER JOIN city USING (cityId)\n" +
                       "INNER JOIN country USING (countryId);";

        QueryManager.makeQuery(sqlStatement);
        resultSet = QueryManager.getResult();

        while (resultSet.next()) {

            int customerId = resultSet.getInt("customerId");
            int addressId = resultSet.getInt("addressId");
            int cityId = resultSet.getInt("cityId");
            int countryId = resultSet.getInt("countryId");
            String customerName = resultSet.getString("customerName");
            int active = resultSet.getInt("active");
            String addressLn1 = resultSet.getString("address");
            String addressLn2 = resultSet.getString("address2");
            String postalCode = resultSet.getString("postalCode");
            String phoneNumber = resultSet.getString("phone");
            String cityName = resultSet.getString("city");
            String countryName = resultSet.getString("country");

            Customer newCustomer = new Customer(customerId, addressId, cityId, countryId, customerName, active,
                                                addressLn1, addressLn2, postalCode, phoneNumber, cityName, countryName);
            inv.addCustomer(newCustomer);
        }

    }


    //TODO clean
    //Conversion for pulling TimeStamps from DB
    public static String convertUTCToLocal(Timestamp inputTimestamp) {

        //System.out.println(inputTimestamp + "  inputTimestamp");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime localDateTime_UTC = inputTimestamp.toLocalDateTime();
        //System.out.println(localDateTime_UTC + "  localDateTime_UTC");

        ZonedDateTime zonedDateTime_UTC = ZonedDateTime.of(localDateTime_UTC.toLocalDate(), localDateTime_UTC.toLocalTime(), UTC);
        //System.out.println(zonedDateTime_UTC + "  zonedDateTime_UTC");

        ZonedDateTime zonedDateTime_system = zonedDateTime_UTC.withZoneSameInstant(ZoneId.systemDefault());
        //System.out.println(zonedDateTime_system + "  zonedDateTime_system");

        LocalDateTime localDateTime_system = zonedDateTime_system.toLocalDateTime();
        //System.out.println(localDateTime_system + "  localDateTime_system");

        String convertedString;
        return convertedString = localDateTime_system.format(formatter);

    }


}