package com.marrok.inventaire_esm.util;

import com.marrok.inventaire_esm.controller.dashboard.DashboardController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralUtil {


    private static final double MINIMUM_WINDOW_WIDTH = 390.0;
    private static final double MINIMUM_WINDOW_HEIGHT = 500.0;


    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        if (alertType == Alert.AlertType.INFORMATION) {
            // Create a timeline to close the alert after the specified timeout
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(500),
                    event -> alert.close()
            ));
            timeline.setCycleCount(1);
            timeline.play();
        }
        alert.showAndWait();
    }

    public static void showAlertWithOutTimelimit(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void goBackDashboard(ActionEvent event) {
        loadScene("/com/marrok/inventaire_esm/view/dashboard/dashboard-view.fxml",event,true);

    }
    public static void goBackStockDashboard(ActionEvent event) {
        loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml",event,true);

    }


    public static void goBackLogin(ActionEvent event) {
        loadScene("/com/marrok/inventaire_esm/view/login/login-view.fxml",event,true);
    }
//    private static BufferedImage generateBarcode(String text) throws WriterException {
//        int width = 300;
//        int height = 100;
//        BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);
//        return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }



    public static void loadScene(String resourcePath, ActionEvent event, boolean isResizable) {
        FXMLLoader loader = new FXMLLoader(GeneralUtil.class.getResource(resourcePath));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setCursor(Cursor.HAND);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image(GeneralUtil.class.getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));

            // Check if the resource path is the login view; if so, don't set dimensions to maximize
            if (resourcePath.contains("login-view")) {
                // Let the stage size itself based on the scene’s preferred size
                stage.sizeToScene();
            } else {
                // For other views, maximize the stage
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
            }

            stage.setScene(scene);
            stage.setResizable(isResizable);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error loading scene: " + resourcePath, ex);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر تحميل المشهد المطلوب. يرجى المحاولة مرة أخرى لاحقًا.");
        }
    }


}
