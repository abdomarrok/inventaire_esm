package com.marrok.inventaire_esm.controller.employer;

import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField titleField;

    private EmployerController employerController;
    private Employer currentEmployer;
    DatabaseHelper dbhelper=new DatabaseHelper();

    public UpdateController() throws SQLException {
    }

    public void setEmployerController(EmployerController employerController) {
        this.employerController = employerController;
    }

    public void setEmployerData(Employer employer) {
        this.currentEmployer = employer;
        firstNameField.setText(employer.getFirstName());
        lastNameField.setText(employer.getLastName());
        titleField.setText(employer.getTitle());
    }

    @FXML
    private void handleUpdateEmployer(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String title = titleField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || title.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        currentEmployer.setFirstName(firstName);
        currentEmployer.setLastName(lastName);
        currentEmployer.setTitle(title);

        boolean success = dbhelper.updateEmployer(currentEmployer);

        if (success) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Employer updated successfully.");
            employerController.refreshTableData();
            closeWindow();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employer.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
