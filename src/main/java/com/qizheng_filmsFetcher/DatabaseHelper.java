package com.qizheng_filmsFetcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseHelper {
    // Database connection details (user and password can be moved to .env in the future for security reason)
    private static final String DB_URL = "jdbc:mysql://pa-datasources-cluster.cluster-ctkeuweqhlpx.us-east-1.rds.amazonaws.com:3306/assessment";
    private static final String DB_USER = "testuser";
    private static final String DB_PASSWORD = "Test@P4rr0t!";

    // Get a connection to the RDS database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Method to ensure the specified table exists in the database
    public static void ensureTableExists(String tableName) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(255), "
                + "rating VARCHAR(100), "
                + "release_year VARCHAR(255), "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        // Try-with-resources to auto-close the connection and statement
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table " + tableName + " ensured to exist.");
        } catch (SQLException e) {
            System.err.println("Error occurred while ensuring table " + tableName + " exists: " + e.getMessage());
        }
    }

    // Method to batch insert a list of movies into the specified table
    public static void batchInsertMovies(List<Movie> movies, String tableName) {
        ensureTableExists(tableName);

        String insertSQL = "INSERT INTO " + tableName + " (title, rating, release_year) VALUES (?, ?, ?)";

        //Auto-close the connection and prepared statement
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Disable auto-commit for batch processing
            connection.setAutoCommit(false);

            // Iterate through the movies list and add to the batch
            for (Movie movie : movies) {
                preparedStatement.setString(1, movie.getTitle());
                preparedStatement.setString(2, movie.getRating());
                preparedStatement.setString(3, movie.getReleaseYear());
                preparedStatement.addBatch();
            }

            // Execute the batch and commit the transaction
            preparedStatement.executeBatch();
            connection.commit();

            System.out.println("Movies inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error occurred while inserting movies: " + e.getMessage());
        }
    }
}
