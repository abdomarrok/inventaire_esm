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
}
