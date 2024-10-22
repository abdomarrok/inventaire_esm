package com.marrok.inventaire_esm.controller.bon_sortie;

import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.BonSortie;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
        id_bon_sortie.setCellValueFactory(new  PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
}
