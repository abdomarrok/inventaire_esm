package com.marrok.inventaire_esm.controller.employer;

import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class UpdateController {
    Logger logger = Logger.getLogger(UpdateController.class);
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField titleField;

    private EmployerController employerController;
    private Employer currentEmployer;
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public UpdateController() throws SQLException {
    }

    public void setEmployerController(EmployerController employerController) {
        this.employerController = employerController;
    }

    public void setEmployerData(Employer employer) {
        logger.info("setEmployerData");
        this.currentEmployer = employer;
        firstNameField.setText(employer.getFirstName());
        lastNameField.setText(employer.getLastName());
        titleField.setText(employer.getTitle());
    }

    @FXML
    private void handleUpdateEmployer(ActionEvent event) {
        logger.info("handleUpdateEmployer");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String title = titleField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || title.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "يرجى ملء جميع الحقول.");

            return;
        }

        currentEmployer.setFirstName(firstName);
        currentEmployer.setLastName(lastName);
        currentEmployer.setTitle(title);

        boolean success = employerDbHelper.updateEmployer(currentEmployer);

        if (success) {
            logger.info("updateEmployer success");
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم تحديث الموظف بنجاح.");

            employerController.refreshTableData();
            closeWindow();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحديث الموظف.");

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
