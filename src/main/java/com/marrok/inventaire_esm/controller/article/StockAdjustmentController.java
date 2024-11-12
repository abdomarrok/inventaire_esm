package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.controller.bon_entree.DetailViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StockAdjustmentController {
    public void goAddAdjustemnt(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/article/add_adjustement.fxml"));
            Parent root = loader.load();

            // Get the controller and set the selected BonEntree
            AddAdjustmentController controller = loader.getController();
          //  controller.setBonEntree(selectedBonEntree);

            // Create a new scene and set it to the stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add adjustemnt");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
