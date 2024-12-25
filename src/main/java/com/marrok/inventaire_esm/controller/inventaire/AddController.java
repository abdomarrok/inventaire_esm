package com.marrok.inventaire_esm.controller.inventaire;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.*;
import fr.brouillard.oss.cssfx.CSSFX;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Localisation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    Logger logger = LogManager.getLogger(AddController.class);

    public ChoiceBox<String> status_inventaire;
    public DatePicker calendarPicker1;

    public ChoiceBox<String> locationChoiceBox;

   public TableView<Employer> tbData2;
   public TableColumn<Employer, Integer> id_E;
//   public TableColumn<Employer, String> firstname_E;
//    public TableColumn<Employer, String> lastname_E;
   public TableColumn<Employer, String> name_E;

    public Button saveButton;
    public FilterView<Article> filterView;
    public FilterView<Employer> filterView2;
    public TableView<Article> tbData;
    public TableColumn<Article, Integer> id;
    public TableColumn<Article, String> article_name;
    private ObservableList<Article> articlelist;
    private ObservableList<Employer> emploerlist;
    public Button cancelButton;

    private InventaireItemController parentController;
    public TextField employerInventaireCode;

    String[] inv_status = {"BON ETAT","MOYEN","MAUVAIS","VER ANNEXE-HARRACHE","EN PANNE"};
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    LocDbhelper locDbhelper = new LocDbhelper();
    InventaireItemDbHelper inventaireItemDbHelper = new InventaireItemDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public AddController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing AddController");
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
        logger.info("Loading filter");
        filterView.getFilterGroups().clear();
        filterView2.getFilterGroups().clear();
        filterView.setTextFilterProvider(text-> article -> {
            if(text==null||text.isEmpty()){
                return true;
            }

            String lowerCase = text.toLowerCase();
            return article.getName().toLowerCase().contains(lowerCase)||
                    String.valueOf(article.getId()).toLowerCase().contains(lowerCase);

        });


        filterView2.setTextFilterProvider(text -> employer -> {
            if(text==null || text.isEmpty()){
                return true;
            }
            String lowerCase = text.toLowerCase();
            return employer.getFirstName().toLowerCase().contains(lowerCase)||
                   employer.getLastName().toLowerCase().contains(lowerCase)||
                    String.valueOf(employer.getId()).toLowerCase().contains(lowerCase);
        });
        loadTableData();
        SortedList<Article> sortedList = new SortedList<>(filterView.getFilteredItems());
        SortedList<Employer> sortedList2 = new SortedList<>(filterView2.getFilteredItems());
        tbData.setItems(sortedList);
        tbData2.setItems(sortedList2);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
        sortedList2.comparatorProperty().bind(tbData2.comparatorProperty());
    }
    private void initTable() {
        logger.info("Initializing table");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        article_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        /**    EMPLOYER   */
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_E.setCellValueFactory(cellData->{
          String fname=  cellData.getValue().getFirstName();
          String lname=  cellData.getValue().getLastName();
          return new SimpleStringProperty(fname+" "+lname);
        });
//        firstname_E.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//        lastname_E.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    private void loadTableData() throws SQLException {
        logger.info("Loading table data");
        List<Article> articles = articleDbhelper.getArticles();
        List<Employer> employers = employerDbHelper.getEmployers();

        articlelist =  FXCollections.observableArrayList(articles);
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView.getItems().setAll(articlelist);
        filterView2.getItems().setAll(emploerlist);
    }

    public void handleAdd(ActionEvent event) {
        logger.info("handleAdd");
        // Get the selected article from the TableView
        Article selectedArticle = tbData.getSelectionModel().getSelectedItem();
        Employer selectedEmployer=tbData2.getSelectionModel().getSelectedItem();

        if (selectedArticle != null  && selectedEmployer != null) {
            int articleId = selectedArticle.getId(); // Get the ID from the selected article
            int employerId = selectedEmployer.getId();
            String selectedLocation = locationChoiceBox.getValue();
            // Assuming the format is "الطابق: <floor> <locationName>"
            String[] parts = selectedLocation.split(" {3,}");
            String locationName = parts[1]; // This gets the location name after the floor number

            int localisationId = locDbhelper.getLocationIdByName(locationName);
            int userId = SessionManager.getActiveUserId(); // Fetch the logged-in user from SessionManager

            String inv_status = status_inventaire.getValue();
            LocalDate selectedDate = calendarPicker1.getValue();
            String inv_date = selectedDate != null ? selectedDate.toString() : "";


            if (employerId != -1 && employerInventaireCode.getText() != null) { // Check if employer was successfully found
                Inventaire_Item newItem = new Inventaire_Item(0, articleId,
                        localisationId, userId, employerId, employerInventaireCode.getText(),inv_date,inv_status);

                if (inventaireItemDbHelper.addInventaireItem(newItem)) {
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



    public void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void loadChoiceBoxData() {
        logger.info("loadChoiceBoxData called");

      //  employerChoiceBox.getItems().addAll(employers);

        List<Localisation> Locations = locDbhelper.getLocalisations();
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
