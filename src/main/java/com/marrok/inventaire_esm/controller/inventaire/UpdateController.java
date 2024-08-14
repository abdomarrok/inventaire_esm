package com.marrok.inventaire_esm.controller.inventaire;

import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class UpdateController  implements Initializable {



    @FXML
    private ChoiceBox<String> locationChoiceBox;

    @FXML
    private ChoiceBox<String> articleChoiceBox;

    @FXML
    private ChoiceBox<String> employerChoiceBox;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private InventaireItemController parentController;
    private Inventaire_Item inventaireItem;
    @FXML
    private TextField employerInventaireCode;
    DatabaseHelper dbhelper = new DatabaseHelper();

    public UpdateController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadChoiceBoxData();
    }

    public void setParentController(InventaireItemController parentController) {
        this.parentController = parentController;
    }

    public void setInventaireItem(Inventaire_Item inventaireItem) {
        this.inventaireItem = inventaireItem;
        try{
            String selectedloc = dbhelper.getLocalisationById(inventaireItem.getLocalisation_id()).getLocName();
            String selectedArticle=dbhelper.getArticleById(inventaireItem.getArticle_id()).getName();
            String selectedEmploer=dbhelper.getEmployerFullNameById(inventaireItem.getEmployer_id());
            locationChoiceBox.setValue(selectedloc);
            articleChoiceBox.setValue(selectedArticle);
            employerChoiceBox.setValue(selectedEmploer);
        }catch (Exception e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }

        employerInventaireCode.setText(inventaireItem.getNum_inventaire());
    }

    @FXML
    private void handleUpdate() {
        try{
        int loc_id =dbhelper.getLocationIdByName(locationChoiceBox.getSelectionModel().getSelectedItem());
        int article_id = dbhelper.getArticleIdByName( articleChoiceBox.getValue());
        int employer_id = dbhelper.getEmployerIdByName(employerChoiceBox.getSelectionModel().getSelectedItem());
        int inv_id=inventaireItem.getId();
        int user_id= SessionManager.getActiveUserId();
        Inventaire_Item updated_item = new Inventaire_Item(inv_id, article_id, loc_id, user_id, employer_id,employerInventaireCode.getText());


            dbhelper.updateInventaireItem(updated_item);

            parentController.refreshTableData(); // Refresh the table data in the parent controller
            closeWindow();
            // Optionally, show a success message
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            closeWindow();
        }

    }
    private void loadChoiceBoxData() {
        List<String> employers = dbhelper.getAllEmployersNames();
        List<String> articles = dbhelper.getAllArticlesNames();
        List<String> locations = dbhelper.getAllLocations();

        employerChoiceBox.getItems().addAll(employers);
        articleChoiceBox.getItems().addAll(articles);
        locationChoiceBox.getItems().addAll(locations);
    }

    @FXML
    private void handleCancel() {
        // Close the update form
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}
