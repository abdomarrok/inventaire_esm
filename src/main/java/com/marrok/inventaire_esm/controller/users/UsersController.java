package com.marrok.inventaire_esm.controller.users;

import com.marrok.inventaire_esm.model.User;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    private static final Logger logger = LogManager.getLogger(UsersController.class);
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



    private ObservableList<User> userList;
    private FilteredList<User> filteredUserList;
    private User selectedUser;
    private UserDbHelper dbHelper;

    {
        try {
            dbHelper = new UserDbHelper();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing UsersController");
        initializeColumns();
        loadData();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {
        logger.info("Initializing UsersController Columns");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

   public void loadData() {
        logger.info("Loading UsersController data");
       userList = FXCollections.observableArrayList(dbHelper.getUsers());
       filteredUserList = new FilteredList<>(userList, p -> true);
       usersTableView.setItems(filteredUserList);
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
        logger.info("Adding new user");
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
             logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في فتح نموذج إضافة المستخدم.");

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
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في فتح نموذج إضافة المستخدم.");

        }
    }


    @FXML
    private void deleteUser(ActionEvent event) {
        if (selectedUser != null) {
            if (dbHelper.deleteUser(selectedUser.getId())) {
                userList.remove(selectedUser);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم حذف المستخدم", "تم حذف المستخدم بنجاح.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مستخدم للحذف.");
        }

    }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }



}
