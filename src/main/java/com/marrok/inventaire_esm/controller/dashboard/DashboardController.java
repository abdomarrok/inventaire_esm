package com.marrok.inventaire_esm.controller.dashboard;

import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController extends AnchorPane implements Initializable {
    @FXML
    private Button employers_button;
    @FXML
    private Button product_button;
    @FXML
    private Button category_button;
    @FXML
    private Button services_button;
    @FXML
    private Button location_button;
    @FXML
    private Button inventaire_button;
    @FXML
    private Button settings_button;
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
                settings_button.setDisable(true);
                break;
            case "User":
                // User sees limited options
                employers_button.setDisable(true);
                settings_button.setDisable(true);
                services_button.setDisable(true);
                break;
            default:
                employers_button.setDisable(true);
                product_button.setDisable(true);
                category_button.setDisable(true);
                services_button.setDisable(true);
                location_button.setDisable(true);
                inventaire_button.setDisable(true);
                settings_button.setDisable(true);
                break;
        }
    }


    @FXML
    private void go_Setting(ActionEvent event){
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/settings/settings-view.fxml", event,true);
    }
    @FXML
    private void logoutAction(ActionEvent event) {

        GeneralUtil.goBackLogin(event);
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


    public void go_StockDashboard(ActionEvent event) {
        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml", event,true);
    }
}
