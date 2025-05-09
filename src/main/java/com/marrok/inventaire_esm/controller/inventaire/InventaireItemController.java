package com.marrok.inventaire_esm.controller.inventaire;


import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.*;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InventaireItemController implements Initializable {
    Logger logger = LogManager.getLogger(InventaireItemController.class);


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
    private int user_id = -1;
    private String user_role = null;
    private UserDbHelper dbhelper = new UserDbHelper();;


    public Label titleLabel;

    private ObservableList<Inventaire_Item> inventaireItemList;
    private FilteredList<Inventaire_Item> filteredInventaireItemList;
    private Inventaire_Item selectedInventaireItem;

    ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    LocDbhelper locDbhelper = new LocDbhelper();
    InventaireItemDbHelper inventaireItemDbHelper = new InventaireItemDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    public InventaireItemController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("InventaireItemController initialize");
        checkUserRole();
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void checkUserRole() {
        logger.info("InventaireItemController checkUserRole");
        // Initialize theme properties
        user_id = SessionManager.getActiveUserId();
        if (user_id != -1) {
            user_role = dbhelper.getUserRoleById(user_id);

            if (user_role != null) {
                customizeInventaireViewForRole(user_role);
            }else {
               logger.error("user_role is null");

            }
        }
    }

    private void customizeInventaireViewForRole(String role) {
        logger.info("InventaireItemController customizeInventaireViewForRole");
        switch (role) {
            case "Admin":
                // Admin sees everything
                break;
            case "Editor":
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                break;
            case "User":
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
                addButton.setDisable(true);
                break;
            default:

                break;
        }
    }

    private void initializeColumns() {
        logger.info("InventaireItemController initializeColumns");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        localisationIdColumn.setCellValueFactory(cellData -> {
            int idLocalisation = cellData.getValue().getLocalisation_id();

            // Fetch all locations
            List<Localisation> Locations = locDbhelper.getLocalisations();
            List<String> locations_and_floor = new ArrayList<>();

            // Create a list of location descriptions (floor and name)
            for (Localisation l : Locations) {
                locations_and_floor.add("الطابق: " + l.getFloor() + "   " + l.getLocName());
            }

            // Get the localisation by ID
            Localisation localisation = locDbhelper.getLocalisationById(idLocalisation);

            if (localisation != null) {
                // Find the location in the list and return the corresponding name
                for (String location : locations_and_floor) {
                    if (location.contains(localisation.getLocName())) {
                        return new SimpleStringProperty(location); // Return the full location description
                    }
                }
            }

            // Fallback if the location is not found
            return new SimpleStringProperty("Unknown Location");
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
                String articleName = articleDbhelper.getArticleById(articleId).getName();

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
                String employerName = employerDbHelper.getEmployerFullNameById(employerId);
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
        logger.info("InventaireItemController loadData");
        inventaireItemList = FXCollections.observableArrayList(inventaireItemDbHelper.getInventaireItems());
        filteredInventaireItemList = new FilteredList<>(inventaireItemList, p -> true);
        tableView.setItems(filteredInventaireItemList);
    }

    private void setupSearchFilter() {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredInventaireItemList.setPredicate(item -> {
                String emp_name = employerDbHelper.getEmployerFullNameById(item.getEmployer_id());
                String art_name = articleDbhelper.getArticleById(item.getArticle_id()).getName();
                String loc_name = locDbhelper.getLocalisationById(item.getLocalisation_id()).getLocName();
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
        bk_Dashboard_from_inventaireitem.setOnAction(GeneralUtil::goBackStockDashboard);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedInventaireItem = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                Inventaire_Item selectedInventaire = tableView.getSelectionModel().getSelectedItem();
                showInventaireDetails(selectedInventaire.getId());
            }
        });
    }


    private void showInventaireDetails(int id) {
        logger.info("InventaireItemController showInventaireDetails");
        try {
            Inventaire_Item selectedInventaire = inventaireItemDbHelper.getInevntaireItemById(id);
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
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحميل عرض تفاصيل عنصر الجرد.");

        }

    }


    public void addInventaireItem(ActionEvent event) {
        logger.info("InventaireItemController addInventaireItem");
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
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة عنصر الجرد.");
        }
    }

    @FXML
    public void updateInventaireItem(ActionEvent event) {
        logger.info("InventaireItemController updateInventaireItem");
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
                logger.error(e);
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج تحديث عنصر الجرد.");
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
        logger.info("InventaireItemController deleteInventaireItem");
        Inventaire_Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            boolean test = GeneralUtil.showConfirmationDialog("تاكيد", "هل متاكد انك تريد حذف هذا الجرد" + selectedItem.getNum_inventaire());
            if(test){
                try {
                    boolean isDeleted = inventaireItemDbHelper.deleteInventaireItem(selectedItem.getId());
                    if (isDeleted) {
                        logger.info("InventaireItemController deleteed InventaireItem");
                        refreshTableData();
                        GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم حذف العنصر بنجاح.");

                    } else {
                        GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر حذف العنصر.");

                    }
                } catch (Exception e) {
                    logger.error(e);
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "حدث خطأ أثناء محاولة حذف العنصر.");

                }
            }

        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار عنصر للحذف.");
        }
    }



    public ObservableList<Inventaire_Item> getInventaireItemList() {
        return inventaireItemList;
    }

    public void refreshTableData() {
        inventaireItemList.setAll(inventaireItemDbHelper.getInventaireItems());
        tableView.refresh();
    }
}
