package com.marrok.inventaire_esm.controller.bon_entree;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.BonEntreeDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.FournisseurDbHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BonEntreeController implements Initializable {
    public TableView<BonEntree> tableView;
    public TableColumn<BonEntree,String> document_num;
    public TableColumn<BonEntree,Integer> id_bon_entree;
    public TableColumn<BonEntree,String> fournisseur;
    public TableColumn<BonEntree,String> date;
    public TextField searchField;
    private ObservableList<BonEntree> bonEntreesList;
    private FilteredList<BonEntree> filtredbonEntreesList;
    private BonEntree selectedBonEntree;

    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();
    private BonEntreeDbHelper bonEntreeDbHelper =  new BonEntreeDbHelper();

    public BonEntreeController() throws SQLException {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        document_num.setCellValueFactory(new PropertyValueFactory<>("documentNum"));
        id_bon_entree.setCellValueFactory(new  PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
      //  fournisseur.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        fournisseur.setCellValueFactory(celldata->{
            int idfournisseur= celldata.getValue().getIdFournisseur();
            Fournisseur fournisseur1 = fournisseurDbHelper.getFournisseurById(idfournisseur);
            if(fournisseur1!=null){
                return new SimpleStringProperty(fournisseur1.getName());
            }else {
                return new SimpleStringProperty("مورد غير معروف");
            }
        });
    }

    private void loadData() {
        List<BonEntree> bonEntrees=bonEntreeDbHelper.getBonEntrees();
        bonEntreesList = FXCollections.observableArrayList(bonEntrees);
        filtredbonEntreesList = new FilteredList<>(bonEntreesList, p -> true);
        tableView.setItems(filtredbonEntreesList);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> articleNameList = new ArrayList<>();

            filtredbonEntreesList.setPredicate(bonEntree -> {
                // If the search field is empty, return all entries.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                int bonEntreeId = bonEntree.getId();
                List<Entree> entrees = bonEntreeDbHelper.getEntreesById_BonEntreeId(bonEntreeId);
                if (entrees != null) {
                    for (Entree entree : entrees) {
                        // Added missing closing parenthesis.
                        Article article = articleDbhelper.getArticleById(entree.getIdArticle());
                        if (article != null) {
                            articleNameList.add(article.getName());
                        }
                    }
                }

                // Convert the search query to lowercase for case-insensitive matching.
                String lowerCaseFilter = newValue.toLowerCase();
                return bonEntree.getDocumentNum().contains(lowerCaseFilter)
                        || bonEntree.getDate().toString().contains(lowerCaseFilter)
                        || String.valueOf(bonEntree.getId()).contains(lowerCaseFilter)
                        || String.valueOf(bonEntree.getIdFournisseur()).contains(lowerCaseFilter);

            });
        });
    }

    private void setupTableSelectionListener() {

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedBonEntree = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                BonEntree selectedBonEntree = tableView.getSelectionModel().getSelectedItem();
                showBonEntreeDetails(selectedBonEntree.getId());
            }
        });

    }

    private void showBonEntreeDetails(int selected_be_id) {
        BonEntree selectedBonEntree = bonEntreeDbHelper.getBonEntreesById(selected_be_id);
        if (selectedBonEntree != null) {
            try {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_entree/detail-view.fxml"));
                Parent root = loader.load();

                // Get the controller and set the selected BonEntree
                DetailViewController controller = loader.getController();
                controller.setBonEntree(selectedBonEntree);

                // Create a new scene and set it to the stage
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Bon Entree Details");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error message)
            }
        } else {
            // Handle case where BonEntree is not found (e.g., show a message)
            System.out.println("BonEntree not found.");
        }
    }



    public void goHome(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }
    public void refreshTableData() {
            List<BonEntree> bonEntrees = bonEntreeDbHelper.getBonEntrees();
            bonEntreesList.setAll(bonEntrees);
            tableView.refresh();

    }


    public void goBonEntree(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_entree/add_bon_entree-view.fxml"));
            Parent root = loader.load();
            AddBonEntreeController controller = loader.getController();//من اجل ارسال متغيرات عبره
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("وصل ادخال");
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
