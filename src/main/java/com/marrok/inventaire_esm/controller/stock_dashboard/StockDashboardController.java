package com.marrok.inventaire_esm.controller.stock_dashboard;

import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StockDashboardController implements Initializable {
    public Button etat_stock_button;
    public Button list_be_button;
    public Button list_bs_button;
    public Button product_button;
    public Button category_button;
    public Button fornisseurs_button;
    private int user_id = -1;
    private String user_role = null;
    private UserDbHelper dbhelper;

    {
        try {
            dbhelper = new UserDbHelper();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize theme properties
        user_id = SessionManager.getActiveUserId();
        if (user_id != -1) {
            user_role = dbhelper.getUserRoleById(user_id);

            if (user_role != null) {
                customizeDashboardForRole(user_role);
            }else {
                System.out.println("user_role is null");

            }
        }

    }

    private void customizeDashboardForRole(String role) {
        switch (role) {
            case "Admin":
                // Admin sees everything
                break;
            case "Editor":
                product_button.setDisable(true);
                fornisseurs_button.setDisable(true);
                break;
            case "User":
                // User sees limited options
                category_button.setDisable(true);
                product_button.setDisable(true);
                fornisseurs_button.setDisable(true);
                break;
            default:
                etat_stock_button.setDisable(true);
                        list_be_button.setDisable(true);
                list_bs_button.setDisable(true);
                category_button.setDisable(true);
                product_button.setDisable(true);
                fornisseurs_button.setDisable(true);
                break;
        }
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

    public void go_bon_entrees_list(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_entree/bon_entree-view.fxml",event,true);

    }
    public void go_bon_sorties_list(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_sortie/bon_sortie-view.fxml",event,true);

    }
}
