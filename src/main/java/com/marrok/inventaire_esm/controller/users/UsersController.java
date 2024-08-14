package com.marrok.inventaire_esm.controller.users;

import com.marrok.inventaire_esm.model.User;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button addUserButton;

    @FXML
    private Button updateUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button bk_Dashboard_from_users;

    @FXML
    private Label titleLabel;

    private ObservableList<User> userList;
    private FilteredList<User> filteredUserList;
    private User selectedUser;
    private DatabaseHelper dbHelper;

    {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        loadData();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

   public void loadData() {
        try {
            userList = FXCollections.observableArrayList(dbHelper.getUsers());
            filteredUserList = new FilteredList<>(userList, p -> true);
            usersTableView.setItems(filteredUserList);
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user data.");
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUserList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getUsername().toLowerCase().contains(lowerCaseFilter)
                        || user.getRole().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(user.getId()).contains(lowerCaseFilter);
            });
        });
    }

    private void setupTableSelectionListener() {
        usersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedUser = newValue);
    }

    @FXML
    private void addUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/users/add_form-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add User");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddUserController controller = loader.getController();
            controller.setUsersController(this);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add user form.");
        }
    }

    public void updateUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/users/update_form-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("update User");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            UpdateUserController controller = loader.getController();
            controller.setUser(selectedUser);
            controller.setUsersController(this);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add user form.");
        }
    }


    @FXML
    private void deleteUser(ActionEvent event) {
        if (selectedUser != null) {
            try {
                if (dbHelper.deleteUser(selectedUser.getId())) {
                    userList.remove(selectedUser);
                    GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "User Deleted", "The user was deleted successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
        }
    }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        GeneralUtil.goBackDashboard(event);
    }

    public void go_Dashboard(ActionEvent event) {
        GeneralUtil.goBackDashboard(event);
    }


}
