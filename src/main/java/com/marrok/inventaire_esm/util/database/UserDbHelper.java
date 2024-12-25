package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.User;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDbHelper {
    private static final Logger logger = LogManager.getLogger(UserDbHelper.class);
    public UserDbHelper() throws SQLException {
        logger.info("UserDbHelper instantiated");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public Connection cnn;

    public boolean validateLogin(String username, String password) {
        logger.info("validateLogin called");
        String query = "SELECT id FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    SessionManager.setActiveUserId(userId); // Set the active user ID
                    return true;
                }
            }
        } catch (SQLException e) {
           logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"ERROR",e.getMessage());
            // Handle exception
        }
        return false;
    }


    public String getUserNameById(int userId) {
        logger.info("getUserNameById called");
        String query = "SELECT username FROM user WHERE id = ?";
        String username = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"ERROR","username not found");
        }

        return username;
    }
    public String getUserRoleById(int userId) {
        logger.info("getUserRoleById called");
        String query = "SELECT role FROM user WHERE id = ?";
        String role = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } catch (SQLException e) {
            logger.error(e);
            // Handle exception, log error or throw a custom exception
        }

        return role;
    }

    public boolean addUser(User user) {
        logger.info("addUser called");
        String query = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)){

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
             logger.error(e);
            return false;
        }
    }

    public void updateUser(User user)  {
        logger.info("updateUser called");
        String query = "UPDATE user SET username = ?, password = ?, role = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
             logger.error(e);
        }
    }

    public int getUserIdByName(String username) {
        logger.info("getUserIdByName called");
        String query = "SELECT id FROM user WHERE username = ?";
        int userId = -1; // Return -1 if the user is not found

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
             logger.error(e);
            // Handle exception
        }

        return userId;
    }


    public List<User> getUsers() {
        logger.info("getUsers called");
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, role FROM user ORDER BY id DESC;";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                User user = new User(id, username, password, role);
                users.add(user);
            }
        } catch (SQLException e) {
             logger.error(e);
           // throw new SQLException("Failed to fetch users from the database", e);
        }

        return users;
    }

    // Example method for deleting a user
    public boolean deleteUser(int userId) {
        logger.info("deleteUser called");
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
             logger.error(e);
             GeneralUtil.showAlert(Alert.AlertType.ERROR,"ERROR", "لا تستطيع حذف هذا المستخدم لانه الجرد الذي انشاه لا يزال موجودا"+e);
        }
        return false;
    }

}
