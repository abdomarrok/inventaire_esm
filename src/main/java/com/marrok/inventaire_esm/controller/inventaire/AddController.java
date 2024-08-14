package com.marrok.inventaire_esm.controller.inventaire;

import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private ChoiceBox<String> locationChoiceBox;
    @FXML
    private ChoiceBox<String> articleChoiceBox;
    @FXML
    private ChoiceBox<String> employerChoiceBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private InventaireItemController parentController;
    @FXML
    private TextField employerInventaireCode;
    DatabaseHelper dbhelper = new DatabaseHelper();

    public AddController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadChoiceBoxData();
    }

    public void setParentController(InventaireItemController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        String selectedLocation = locationChoiceBox.getValue();
        // Assuming the format is "الطابق: <floor> <locationName>"
        String[] parts = selectedLocation.split(" {3,}");
        String locationName = parts[1]; // This gets the location name after the floor number

        int localisationId = dbhelper.getLocationIdByName(locationName);
        int userId = SessionManager.getActiveUserId(); // Fetch the logged-in user from SessionManager
        int articleId = dbhelper.getArticleIdByName(articleChoiceBox.getValue());
        String employerName = employerChoiceBox.getValue();
        int employerId = dbhelper.getEmployerIdByName(employerName);

        if (employerId != -1 && employerInventaireCode != null) { // Check if employer was successfully found
            Inventaire_Item newItem = new Inventaire_Item(0, articleId, localisationId, userId,
                    employerId, employerInventaireCode.getText());

            if (dbhelper.addInventaireItem(newItem)) {
                parentController.refreshTableData();
                closeWindow();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the inventory item.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to find the employer.");
        }
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void loadChoiceBoxData() {
        List<String> employers = dbhelper.getAllEmployersNames();
        List<String> articles = dbhelper.getAllArticlesNames();
        employerChoiceBox.getItems().addAll(employers);
        articleChoiceBox.getItems().addAll(articles);

        List<Localisation> Locations = dbhelper.getLocalisations();
        List<String> locations_and_floor = new ArrayList<>();
        for (Localisation l : Locations) {
            locations_and_floor.add("الطابق: " + l.getFloor() + "   " + l.getLocName());
        }

        locationChoiceBox.getItems().addAll(locations_and_floor);
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
