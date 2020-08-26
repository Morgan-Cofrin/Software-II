package utils;

//Helper class to initiate and terminate connection to the database when the app is launched and closed.

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    //Properties inside
    //JDBC URL parts
    private static final String protocol   = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05hyF";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Driver and Connection interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    //Username
    private static final String username = "U05hyF";
    //Password
    private static final String password = "53688508116";


    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }



    public static Connection startConnection() {

        if (conn != null) {
            return conn;
        } else {
            try {
                Class.forName(MYSQLJDBCDriver);

                conn = DriverManager.getConnection(jdbcURL, username, password);
                System.out.println("Connection Successful");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }
    }

    public static void closeConnection() {

        try {
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Connection closed");
    }


}
