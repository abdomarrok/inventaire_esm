package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Employer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployerDbHelper {
    Logger logger = LogManager.getLogger(EmployerDbHelper.class);

    public Connection cnn;

    public EmployerDbHelper() throws SQLException {
        logger.info("EmployerDbHelper started");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public  int addEmployer(String firstName, String lastName, String title) {
        logger.info("addEmployer started");
        String query = "INSERT INTO employeur (firstname, lastname, title) VALUES (?, ?, ?)";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, title);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                // Retrieve the generated keys to get the ID of the newly inserted employer
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return the ID of the newly inserted employer
                }
            }

            return -1; // Return -1 if the insertion failed or if the ID couldn't be retrieved
        } catch (SQLException e) {
           logger.error(e);
            return -1; // Return -1 if an exception occurred
        }
    }
    public boolean updateEmployer(Employer employer) {
        logger.info("updateEmployer started");
        String query = "UPDATE employeur SET firstname = ?, lastname = ?, title = ? WHERE id = ?";
        try (PreparedStatement pstmt = this.cnn.prepareStatement(query)) {
            pstmt.setString(1, employer.getFirstName());
            pstmt.setString(2, employer.getLastName());
            pstmt.setString(3, employer.getTitle());
            pstmt.setInt(4, employer.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
           logger.error(e);
            return false;
        }
    }

    public Employer getEmployerById(int employerId) {
        logger.info("getEmployerById started");
        Employer employer = null;
        String query = "SELECT * FROM employeur WHERE id = ?"; // Assuming `id` is the primary key in the `employeur` table.

        try (PreparedStatement pstmt = this.cnn.prepareStatement(query)) {

            pstmt.setInt(1, employerId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                // Create an Employer object and set its fields using the result set
                employer = new Employer(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("title")
                );


            }

        } catch (SQLException e) {
           logger.error(e);
        }

        return employer;
    }

    public ObservableList<Employer> getEmployers() {
        logger.info("getEmployers started");
        ObservableList<Employer> employerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM employeur";
        try (Statement stmt = this.cnn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employer employer = new Employer(rs.getInt("id"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("title"));
                employerList.add(employer);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return employerList;
    }
    public boolean deleteEmployer(int employerId) {
        logger.info("deleteEmployer started");
        String query = "DELETE FROM employeur WHERE id = ?";
        try (
                PreparedStatement pstmt = this.cnn.prepareStatement(query)) {
            pstmt.setInt(1, employerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
           logger.error(e);
            return false;
        }
    }
    public  String getEmployerFullNameById(int employerId) {
        logger.info("getEmployerFullNameById started");
        String query = "SELECT firstname, lastname FROM employeur WHERE id = ?";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, employerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                return firstName + " " + lastName;
            } else {
                return null; // Return null if no employer found with the given ID
            }
        } catch (SQLException e) {
          logger.error(e);
            return null; // Return null if an exception occurred
        }
    }

    // Method to fetch all employer names
    public List<String> getAllEmployersNames() {
        logger.info("getAllEmployersNames");
        List<String> employers = new ArrayList<>();
        String query = "SELECT CONCAT(firstname, ' ', lastname) AS fullname FROM employeur";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                employers.add(rs.getString("fullname"));
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return employers;
    }

    public  List<Employer> getAllEmployers() {
        logger.info("getAllEmployers");
        List<Employer> employers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            String query = "SELECT * FROM employeur ORDER BY id DESC";
            statement = this.cnn.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String title = resultSet.getString("title");

                Employer employer = new Employer(id, firstName, lastName, title);
                employers.add(employer);
            }
        } catch (SQLException e) {
            logger.error(e);
            // Handle the exception according to your application's requirements
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
                // Handle the exception according to your application's requirements
            }
        }

        return employers;
    }
    // Method to fetch employer ID by name
    public  int getEmployerIdByName(String fullname) {
        logger.info("getEmployerIdByName started");
        String query = "SELECT id FROM employeur WHERE CONCAT(firstname, ' ', lastname) = ?";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, fullname);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return -1; // Return -1 if not found
    }
}
