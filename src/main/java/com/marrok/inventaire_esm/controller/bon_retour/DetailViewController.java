package com.marrok.inventaire_esm.controller.bon_retour;

import com.marrok.inventaire_esm.model.*;
import com.marrok.inventaire_esm.util.database.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailViewController {
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    private BonRetourDbHelper bonRetourDbHelper = new BonRetourDbHelper();
    private BonRetour bonRetour;
    private Employer currentEmployer;
    private Service currentService;
    private List<Retour> retourList;
    public TableView<Retour> sortieTableView;
    public TableColumn<Retour, String> articleColumn;

    public TableColumn<Retour, Integer> quantityColumn;
    Map<String, Object> parameters = new HashMap<>();

    @FXML
    private Label idLabel, employerLabel, serviceLabel, dateLabel, lastEditedLabel;


    Logger logger = Logger.getLogger(DetailViewController.class);

    public DetailViewController() throws SQLException {
    }

    public void setBonRetour(int id) {
        logger.info("setBonRetour");
    this.bonRetour = bonRetourDbHelper.getBonRetourById(id);
    int bonRetour_id= bonRetour.getId();
    currentEmployer = employerDbHelper.getEmployerById(bonRetour_id);
    currentService = serviceDbHelper.getServiceById(bonRetour_id);
    }

    public void printBonRetour(ActionEvent event) {
    }
}
