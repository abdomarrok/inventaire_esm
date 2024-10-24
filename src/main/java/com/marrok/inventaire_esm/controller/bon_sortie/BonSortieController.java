package com.marrok.inventaire_esm.controller.bon_sortie;

import com.marrok.inventaire_esm.model.BonSortie;
import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.database.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
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
import java.util.List;
import java.util.ResourceBundle;

public class BonSortieController implements Initializable {
    public TableView<BonSortie> tableView;
    public TableColumn<BonSortie,Integer> id_bon_sortie;
    public TableColumn<BonSortie,String> employeur;
    public TableColumn<BonSortie,String> date;
    public TextField searchField;
    private ObservableList<BonSortie> bonSortiesList;
    private FilteredList<BonSortie> filtredbonSortiesList;
    private BonSortie selectedbonSortie;

    DatabaseHelper dbhelper= new DatabaseHelper();

    public BonSortieController() throws SQLException {
    }

    public void goHome(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }



    public void goBonSortie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_sortie/add_bon_sortie-view.fxml"));
            Parent root = loader.load();
            AddBonSortieController controller = loader.getController();//من اجل ارسال متغيرات عبره
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("وصل اخراج");
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedbonSortie = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                BonSortie selectedBonSortie = tableView.getSelectionModel().getSelectedItem();
                showBonSortieDetails(selectedBonSortie.getId());
            }
        });
    }

    private void showBonSortieDetails(int id) {
        System.out.println("test");
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtredbonSortiesList.setPredicate(bonSortie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return bonSortie.getDate().toString().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(bonSortie.getId()).contains(lowerCaseFilter)
                        || String.valueOf(bonSortie.getIdEmployeur()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void loadData() {
        List<BonSortie> bonSorties=dbhelper.getBonSorties();
        bonSortiesList = FXCollections.observableArrayList(bonSorties);
        filtredbonSortiesList = new FilteredList<>(bonSortiesList, p -> true);
        tableView.setItems(filtredbonSortiesList);
    }
    private void initializeColumns() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        employeur.setCellValueFactory(new PropertyValueFactory<>("idEmployeur"));
        employeur.setCellValueFactory(cellData->{
            int idemployer= cellData.getValue().getIdEmployeur();
            Employer employer =dbhelper.getEmployerById(idemployer);
            if(employer!=null){
                return new SimpleStringProperty(employer.getFirstName() + " " + employer.getLastName());
            }else{
                return new SimpleStringProperty("موظف غير معروف");
            }
        });
        id_bon_sortie.setCellValueFactory(new  PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
}
