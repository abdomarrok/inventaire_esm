package com.marrok.inventaire_esm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    public Stage stage;
    private Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;

    private static final double MINIMUM_WINDOW_WIDTH = 390.0;
    private static final double MINIMUM_WINDOW_HEIGHT = 500.0;
    private final Properties themeProperties = new Properties();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/login/login-view.fxml"));
            Parent root = loader.load();

            scene = new Scene(root);
            // Set stage properties
            stage.setTitle("Gestion des Stocks et Inventaire");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }