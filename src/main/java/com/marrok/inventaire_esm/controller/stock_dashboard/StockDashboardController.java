package com.marrok.inventaire_esm.controller.stock_dashboard;

import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StockDashboardController implements Initializable {
    Logger logger = Logger.getLogger(StockDashboardController.class);
    public Button etat_stock_button;
    public Button list_be_button;
    public Button list_bs_button;
    public Button product_button;
    public Button category_button;
    public Button fornisseurs_button;
    public Button employers_button;
    public Button adjustment_button;

    public Button services_button;
    public Button location_button;
    public Button inventaire_button;
    public Button settings_button;
    private int user_id = -1;
    private String user_role = null;
    private UserDbHelper dbhelper = new UserDbHelper();

    public StockDashboardController() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing StockDashboardController");
        checkUserRole();
    }

    private void checkUserRole() {
        // Initialize theme properties
        logger.info("Checking user role");
        user_id = SessionManager.getActiveUserId();
        if (user_id != -1) {
            user_role = dbhelper.getUserRoleById(user_id);

            if (user_role != null) {
                logger.info("User role: " + user_role);
                customizeDashboardForRole(user_role);
            }else {
                logger.info("User role is null");
            }
        }
    }

    private void customizeDashboardForRole(String role) {
        logger.info("Customize dashboard for role: " + role);
        switch (role) {
            case "Admin":
                // Admin sees everything
                break;
            case "Manager":
                settings_button.setDisable(true);
                break;
            case "Editor":
                employers_button.setDisable(true);
                settings_button.setDisable(true);
                break;
            case "User":
                // User sees limited options
                product_button.setDisable(true);
                settings_button.setDisable(true);
                employers_button.setDisable(true);
                settings_button.setDisable(true);
                services_button.setDisable(true);
                category_button.setDisable(true);
                product_button.setDisable(true);
                fornisseurs_button.setDisable(true);
                adjustment_button.setDisable(true);
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
    @FXML
    public void go_Services(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/service/services-view.fxml", event,true);
    }

    @FXML
    public void go_Location(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/location/locations-view.fxml", event,true);
    }

    @FXML
    public void go_Inventaire(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/inventaire/inventaire_item-view.fxml", event,true);
    }

    @FXML
    public void go_Employers(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/employer/employer_view.fxml", event,true);
    }

    @FXML
    private void go_Setting(ActionEvent event){
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/settings/settings-view.fxml", event,true);
    }
    @FXML
    private void logoutAction(ActionEvent event) {

        GeneralUtil.goBackLogin(event);
    }

    public void go_bon_entrees_list(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_entree/bon_entree-view.fxml",event,true);

    }
    public void go_bon_sorties_list(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_sortie/bon_sortie-view.fxml",event,true);

    }

    public void go_Adjustement(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/article/stock_adjustment.fxml",event,true);
    }

    public void go_bon_retours_list(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_retour/bon_retour-view.fxml",event,true);
    }
}
