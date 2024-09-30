package com.marrok.inventaire_esm.controller.inventaire;


import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class InventaireItemController implements Initializable {


    public TableView<Inventaire_Item> tableView;

    public TableColumn<Inventaire_Item, String> localisationIdColumn;

    public TableColumn<Inventaire_Item, String> userIdColumn;

    public TableColumn<Inventaire_Item, Integer> idInventaireColumn;

    public TableColumn<Inventaire_Item, String> articleIdColumn;

    public TableColumn<Inventaire_Item, String> employerIdColumn;

    public TableColumn<Inventaire_Item, String> statusColmun;

    public TableColumn<Inventaire_Item, String> barcodeColumn;

    public TextField searchField;

    public Button addButton;

    public Button bk_Dashboard_from_inventaireitem;

    public  Button btn_fich_inventaire;


    public Button updateButton;

    public Button deleteButton;


    public Label titleLabel;

    private ObservableList<Inventaire_Item> inventaireItemList;
    private FilteredList<Inventaire_Item> filteredInventaireItemList;
    private Inventaire_Item selectedInventaireItem;

    DatabaseHelper dbhelper = new DatabaseHelper();

    public InventaireItemController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);

        // localisationIdColumn.setCellValueFactory(new PropertyValueFactory<>("localisation_id"));
        // Custom cell value factory to get the localisation name by its id
        localisationIdColumn.setCellValueFactory(cellData -> {
            int idLocalisation = cellData.getValue().getLocalisation_id();
            Localisation localisation = dbhelper.getLocalisationById(idLocalisation);

            if (localisation != null) {
                return new SimpleStringProperty(localisation.getLocName());
            } else {
                return new SimpleStringProperty("Unknown Location");
            }
        });

//        userIdColumn.setCellValueFactory(cellData -> {
//            int userId = cellData.getValue().getUser_id();
//            String userName = DatabaseHelper.getUserNameById(userId);
//
//            if (userName != null && !userName.isEmpty()) {
//                return new SimpleStringProperty(userName);
//            } else {
//                return new SimpleStringProperty("Unknown User");
//            }
//        });

        idInventaireColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        articleIdColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getArticle_id();
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                String articleName = dbHelper.getArticleById(articleId).getName();

                if (articleName != null && !articleName.isEmpty()) {
                    return new SimpleStringProperty(articleName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown Article");
            }
            return null;
        });

        employerIdColumn.setCellValueFactory(cellData -> {
            int employerId = cellData.getValue().getEmployer_id();


            if (employerId != 0) {
                String employerName = dbhelper.getEmployerFullNameById(employerId);
                if (employerName != null && !employerName.isEmpty()) {
                    return new SimpleStringProperty(employerName);
                } else {
                    return new SimpleStringProperty("Unknown Employer");
                }
            } else {
                System.out.println("Invalid employer ID: " + employerId);
                return new SimpleStringProperty("Unknown Employer");
            }
        });


        statusColmun.setCellValueFactory(new PropertyValueFactory<>("status"));

        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("num_inventaire"));
    }

    private void loadData() {
        inventaireItemList = FXCollections.observableArrayList(dbhelper.getInventaireItems());
        filteredInventaireItemList = new FilteredList<>(inventaireItemList, p -> true);
        tableView.setItems(filteredInventaireItemList);
    }

    private void setupSearchFilter() {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredInventaireItemList.setPredicate(item -> {
                String emp_name = dbhelper.getEmployerFullNameById(item.getEmployer_id());
                String art_name = dbhelper.getArticleById(item.getArticle_id()).getName();
                String loc_name = dbhelper.getLocalisationById(item.getLocalisation_id()).getLocName();
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getNum_inventaire().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(item.getId()).contains(lowerCaseFilter)
                        || String.valueOf(item.getArticle_id()).contains(lowerCaseFilter)
                        || emp_name.toLowerCase().contains(lowerCaseFilter)
                        || art_name.toLowerCase().contains(lowerCaseFilter)
                        || loc_name.toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(item.getLocalisation_id()).contains(lowerCaseFilter);
            });
        });
    }

    private void setupTableSelectionListener() {
        bk_Dashboard_from_inventaireitem.setOnAction(GeneralUtil::goBackDashboard);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedInventaireItem = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                Inventaire_Item selectedInventaire = tableView.getSelectionModel().getSelectedItem();
                showInventaireDetails(selectedInventaire.getId());
            }
        });
    }


    private void showInventaireDetails(int id) {

        try {
            DatabaseHelper dpHelper = new DatabaseHelper();
            Inventaire_Item selectedInventaire = dpHelper.getInevntaireItemById(id);
            if (selectedInventaire != null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/inventaire/detail-view.fxml"));
                Parent root = loader.load();
                DetailController controller = loader.getController();
                controller.setInventaireDetails(selectedInventaire);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("تفاصيل الجرد");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                stage.show();

            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في استرجاع تفاصيل عنصر الجرد.");

            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحميل عرض تفاصيل عنصر الجرد.");

        }

    }


    public void addInventaireItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/inventaire/add_form-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إضافة عنصر الجرد");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddController controller = loader.getController();
            controller.setParentController(this);

            stage.showAndWait();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة عنصر الجرد.");

            e.printStackTrace();
        }
    }

    @FXML
    public void updateInventaireItem(ActionEvent event) {
        Inventaire_Item selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                // Open a form or dialog to allow the user to update the selected item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/inventaire/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());

                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("تحديث عنصر الجرد");

                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                UpdateController controller = loader.getController();
                controller.setInventaireItem(selectedItem);
                controller.setParentController(this);

                stage.showAndWait();

                // Refresh the table view after the update
                refreshTableData();
            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج تحديث عنصر الجرد.");

                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار عنصر للتحديث.");

        }
    }
    public void goFicheInventaire(ActionEvent event){
      GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/inventaire/fiche_inventaire_view.fxml", event,true);
    }

    @FXML
    public void deleteInventaireItem(ActionEvent event) {
        Inventaire_Item selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            try {
                boolean isDeleted = dbhelper.deleteInventaireItem(selectedItem.getId());
                if (isDeleted) {
                    refreshTableData();
                    GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم حذف العنصر بنجاح.");

                } else {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر حذف العنصر.");

                }
            } catch (Exception e) {
                e.printStackTrace();  // Optional: Log the exception
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "حدث خطأ أثناء محاولة حذف العنصر.");

            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار عنصر للحذف.");
        }
    }



    public ObservableList<Inventaire_Item> getInventaireItemList() {
        return inventaireItemList;
    }

    public void refreshTableData() {
        inventaireItemList.setAll(dbhelper.getInventaireItems());
        tableView.refresh();
    }
}
