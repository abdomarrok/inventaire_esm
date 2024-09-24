package com.marrok.inventaire_esm.controller.inventaire;

import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FicheInventaireController implements Initializable {
    @FXML
    private Button bk_Dashboard_from_fiche_inventaire;
    @FXML
    private ChoiceBox<Integer> inv_year_choiceBox;
    @FXML
    private ChoiceBox<String> selected_service_choiceBox;  // Typed ChoiceBox for Service

    private ObservableList<Service> servicesList;
    private DatabaseHelper dbhelper = new DatabaseHelper();

    // Define available years for the ChoiceBox
    Integer[] availableYears = {2024, 2025, 2026, 2027};

    public FicheInventaireController() throws SQLException {
        // Constructor code if needed
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadServices();
        // Populate the ChoiceBox with years
        inv_year_choiceBox.getItems().addAll(FXCollections.observableArrayList(availableYears));
    }

    @FXML
    private void loadServices() {
        servicesList = FXCollections.observableArrayList(dbhelper.getServices());
        ObservableList<String> serviceNames = FXCollections.observableArrayList();

        for (Service service : servicesList) {
            serviceNames.add(service.getName());
        }

        selected_service_choiceBox.setItems(serviceNames);
    }

    // Handle the extraction of the inventory report
    @FXML
    public void extacteFicheInventaire(ActionEvent event) {
        // Get the selected year and service
        Integer selectedYear = inv_year_choiceBox.getValue();
        String selectedServiceName = selected_service_choiceBox.getValue();

        if (selectedYear == null || selectedServiceName == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select both a year and a service.");
            return; // Exit if selections are invalid
        }

        // Get the corresponding service object from the services list
        Optional<Service> selectedServiceOpt = servicesList.stream()
                .filter(service -> service.getName().equals(selectedServiceName))
                .findFirst();

        if (!selectedServiceOpt.isPresent()) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Service Error", "Selected service not found.");
            return; // Exit if service is not found
        }

        Service selectedService = selectedServiceOpt.get();

        // Logic to extract the inventory report based on selected service and year
        try {
            List<Inventaire_Item> inventoryItems = dbhelper.getInventoryItemsByServiceAndYear(selectedService.getId(), selectedYear);

            if (inventoryItems.isEmpty()) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "No Data", "No inventory items found for the selected year and service.");
                return; // Exit if no items found
            }

            // Generate the report
            generateReport(selectedYear, selectedServiceName, inventoryItems);

        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching the inventory data.");
        }
    }

    private void generateReport(int year, String serviceName, List<Inventaire_Item> items) {
        StringBuilder report = new StringBuilder("Inventory Report\n");
        report.append("Year: ").append(year).append("\n");
        report.append("Service: ").append(serviceName).append("\n\n");
        report.append("Items:\n");

        for (Inventaire_Item item : items) {
            report.append("ID: ").append(item.getId())
                    .append(", Article: ").append(item.getArticle_id())
                    .append(", Quantity: ").append(item.getNum_inventaire()).append("\n");
        }

        // Display the report (replace with actual report generation logic)
        System.out.println(report.toString());
    }

    // Navigate back to the dashboard
    @FXML
    public void goBackDashboard(ActionEvent event) {
        bk_Dashboard_from_fiche_inventaire.setOnAction(GeneralUtil::goBackDashboard);
    }


}
