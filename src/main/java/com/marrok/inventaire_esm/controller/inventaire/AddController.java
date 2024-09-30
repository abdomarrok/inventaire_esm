package com.marrok.inventaire_esm.controller.inventaire;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import fr.brouillard.oss.cssfx.CSSFX;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    public ChoiceBox<String> status_inventaire;
    public CalendarPicker calendarPicker;
    @FXML
    private ChoiceBox<String> locationChoiceBox;

    @FXML
    private ChoiceBox<String> employerChoiceBox;
    @FXML
    private Button saveButton;
    public FilterView<Article> filterView;
    public TableView<Article> tbData;
    public TableColumn<Article, Integer> id;
    public TableColumn<Article, String> article_name;
    private ObservableList<Article> articlelist;
    @FXML
    private Button cancelButton;

    private InventaireItemController parentController;
    @FXML
    private TextField employerInventaireCode;

    String[] inv_status = {"BON ETAT","MOYEN","MAUVAIS","VER ANNEXE-HARRACHE","EN PANNE"};
    DatabaseHelper dbhelper = new DatabaseHelper();

    public AddController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        loadChoiceBoxData();
        try {
            loadFilter();
            loadTableData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CSSFX.start();
    }

    public void setParentController(InventaireItemController parentController) {
        this.parentController = parentController;
    }


    private void loadFilter() throws SQLException {
        filterView.getFilterGroups().clear();
        filterView.setTextFilterProvider(text-> article -> {
            if(text==null||text.isEmpty()){
                return true;
            }

            String lowerCase = text.toLowerCase();
            return article.getName().toLowerCase().contains(lowerCase)||
                    String.valueOf(article.getId()).toLowerCase().contains(lowerCase);

        });
        loadTableData();
        SortedList<Article> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
    }
    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void loadTableData() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<Article> articles = dbHelper.getArticles();
        articlelist =  FXCollections.observableArrayList(articles);
        filterView.getItems().setAll(articlelist); // Set the articles into the table
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        // Get the selected article from the TableView
        Article selectedArticle = tbData.getSelectionModel().getSelectedItem();

        if (selectedArticle != null) {
            int articleId = selectedArticle.getId(); // Get the ID from the selected article

            String selectedLocation = locationChoiceBox.getValue();
            // Assuming the format is "الطابق: <floor> <locationName>"
            String[] parts = selectedLocation.split(" {3,}");
            String locationName = parts[1]; // This gets the location name after the floor number

            int localisationId = dbhelper.getLocationIdByName(locationName);
            int userId = SessionManager.getActiveUserId(); // Fetch the logged-in user from SessionManager
            String employerName = employerChoiceBox.getValue();
            int employerId = dbhelper.getEmployerIdByName(employerName);
            String inv_status = status_inventaire.getValue();
            LocalDate selectedDate = calendarPicker.getValue();
            String inv_date = selectedDate != null ? selectedDate.toString() : "";
            System.out.println("AddController.handleAdd selected date= "+inv_date);

            if (employerId != -1 && employerInventaireCode.getText() != null) { // Check if employer was successfully found
                Inventaire_Item newItem = new Inventaire_Item(0, articleId, localisationId, userId,
                        employerId, employerInventaireCode.getText(),inv_date,inv_status);

                if (dbhelper.addInventaireItem(newItem)) {
                    parentController.refreshTableData();
                    closeWindow();
                } else {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة العنصر إلى المخزون.");
                }
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في العثور على الموظف.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "تحذير", "يرجى اختيار عنصر.");
        }
    }



    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void loadChoiceBoxData() {
        List<String> employers = dbhelper.getAllEmployersNames();
        employerChoiceBox.getItems().addAll(employers);

        List<Localisation> Locations = dbhelper.getLocalisations();
        List<String> locations_and_floor = new ArrayList<>();
        for (Localisation l : Locations) {
            locations_and_floor.add("الطابق: " + l.getFloor() + "   " + l.getLocName());
        }

        locationChoiceBox.getItems().addAll(locations_and_floor);
        status_inventaire.getItems().addAll(FXCollections.observableArrayList(inv_status));
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
