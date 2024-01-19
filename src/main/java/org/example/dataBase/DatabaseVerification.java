package org.example.dataBase;
import java.sql.*;

public class DatabaseVerification {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "userpassword";

    public static boolean isRecordPresent(String natid) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM working_class_heroes WHERE natid = '" + natid + "'";ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isVoucherRecordPresent(String voucherName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM vouchers WHERE name = '" + voucherName + "'";ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Method to delete records with file_status "NEW"
    public static void deleteRecordsWithStatusNew() {
        try {
            // Establish a database connection (modify the connection details accordingly)
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare the SQL statement to delete records
            String sql = "DELETE FROM file WHERE file_status = 'NEW'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Execute the delete statement
            preparedStatement.executeUpdate();

            // Close the resources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
    public static boolean hasRecordsWithStatusNew() {
        try {
            // Establish a database connection (modify the connection details accordingly)
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare the SQL statement to select records with status "NEW"
            String sql = "SELECT * FROM file WHERE file_status = 'NEW'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Execute the select statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there are any records
            boolean hasRecords = resultSet.next();

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

            return hasRecords;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return false;
        }
    }
}
