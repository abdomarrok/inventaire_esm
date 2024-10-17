package com.marrok.inventaire_esm.controller.stock_dashboard;

import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StockDashboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void go_Products(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/article/articles-view.fxml", event,true);
    }

    @FXML
    public void go_Categories(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/category/categories-view.fxml", event,true);
    }

    public void go_Fornisseur(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/fournisseur/fournisseur_view.fxml", event,true);
    }

    public void go_EtatStock(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/article/etat_stock-view.fxml", event,true);
    }

    public void goBack(ActionEvent event) {
        GeneralUtil.goBackDashboard(event);
    }
}
