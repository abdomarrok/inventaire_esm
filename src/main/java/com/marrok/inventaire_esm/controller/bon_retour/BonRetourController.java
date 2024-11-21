package com.marrok.inventaire_esm.controller.bon_retour;

import com.marrok.inventaire_esm.model.BonRetour;

import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.BonRetourDbHelper;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BonRetourController  implements Initializable {
    public TableColumn<BonRetour,String> reason_retour;
    Logger logger = Logger.getLogger(BonRetourController.class);
    public TableView<BonRetour> tableView;
    public TableColumn<BonRetour,Integer> id_bon_retour;
    public TableColumn<BonRetour,String> employeur;
    public TableColumn<BonRetour,String> date;
    public TextField searchField;
    private ObservableList<BonRetour> BonRetoursList;
    private FilteredList<BonRetour> filtredBonRetoursList;
    private BonRetour selectedBonRetour;
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    private BonRetourDbHelper bonRetourDbHelper=new BonRetourDbHelper();

    public BonRetourController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("BonRetourController initialize");
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedBonRetour = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {

            }
        });
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
         filtredBonRetoursList.setPredicate(
                 bonRetour -> {
                     if (newValue == null || newValue.isEmpty()) {
                         return true;
                     }
                     String lowerCaseFilter = newValue.toLowerCase();
                     return bonRetour.getDate().toString().toLowerCase().contains(lowerCaseFilter)
                             || String.valueOf(bonRetour.getId()).contains(lowerCaseFilter)
                             || String.valueOf(bonRetour.getIdEmployeur()).toLowerCase().contains(lowerCaseFilter);

                 }
         );
        });
    }

    private void initializeColumns() {
        logger.info("BonRetourController initializeColumns");
        id_bon_retour.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeur.setCellValueFactory(new PropertyValueFactory<>("idEmployeur"));
        employeur.setCellValueFactory(cellData->{
            int idemployer= cellData.getValue().getIdEmployeur();
            Employer employer =employerDbHelper.getEmployerById(idemployer);
            if(employer!=null){
                return new SimpleStringProperty(employer.getFirstName() + " " + employer.getLastName());
            }else{
                return new SimpleStringProperty("موظف غير معروف");
            }
        });
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        reason_retour.setCellValueFactory(new PropertyValueFactory<>("returnReason"));

    }

    private void loadData() {
        logger.info("BonRetourController loadData");
        List<BonRetour> bonRetours=bonRetourDbHelper.getBonRetours();
        BonRetoursList = FXCollections.observableArrayList(bonRetours);
        filtredBonRetoursList = new FilteredList<>(BonRetoursList, p -> true);
        tableView.setItems(filtredBonRetoursList);
    }

    public void goHome(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }

    public void goBonRetour(ActionEvent event) {
        logger.info("BonRetourController goBonRetour");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_retour/add_bon_retour-view.fxml"));
            Parent root = loader.load();
            AddBonRetourController controller = loader.getController();//من اجل ارسال متغيرات عبره
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("وصل إرجاع");
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.show();
        } catch (IOException e) {
            logger.error(e);
        }
    }

}
