package com.marrok.inventaire_esm.controller.inventaire;

import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.model.Localisation;  // Add Localisation model
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
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
    private ChoiceBox<String> selected_service_choiceBox; // Typed ChoiceBox for Service
    @FXML
    private ChoiceBox<String> selected_localisation_choiceBox; // Optional Localisation choice box

    private ObservableList<Service> servicesList;
    private ObservableList<Localisation> localisationsList;
    private DatabaseHelper dbhelper = new DatabaseHelper();

    // Define available years for the ChoiceBox
    Integer[] availableYears = {2024, 2025, 2026, 2027};

    public FicheInventaireController() throws SQLException {
        // Constructor code if needed
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadServices();
        loadLocalisations(); // Load optional localisations
        // Populate the ChoiceBox with years
        inv_year_choiceBox.getItems().addAll(FXCollections.observableArrayList(availableYears));
    }

    // Load services into the service ChoiceBox
    @FXML
    private void loadServices() {
        servicesList = FXCollections.observableArrayList(dbhelper.getServices());
        ObservableList<String> serviceNames = FXCollections.observableArrayList();

        serviceNames.add("All Services"); // Add "All" option for optional selection
        for (Service service : servicesList) {
            serviceNames.add(service.getName());
        }

        selected_service_choiceBox.setItems(serviceNames);
    }

    // Load localisations into the localisation ChoiceBox
    @FXML
    private void loadLocalisations() {
        localisationsList = FXCollections.observableArrayList(dbhelper.getLocalisations());
        ObservableList<String> localisationNames = FXCollections.observableArrayList();

        localisationNames.add("All Localisations"); // Add "All" option for optional selection
        for (Localisation localisation : localisationsList) {
            localisationNames.add(localisation.getLocName());
        }

        selected_localisation_choiceBox.setItems(localisationNames);
    }

    // Handle the extraction of the inventory report
    @FXML
    public void extacteFicheInventaire(ActionEvent event) {
        // Get the selected year, service, and localisation
        Integer selectedYear = inv_year_choiceBox.getValue();
        String selectedServiceName = selected_service_choiceBox.getValue();
        String selectedLocalisationName = selected_localisation_choiceBox.getValue();

        if (selectedYear == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a year.");
            return; // Exit if year is not selected
        }

        Integer serviceId = null;  // Initialize as null for optional service
        Integer localisationId = null; // Initialize as null for optional localisation

        // If "All Services" is not selected, find the corresponding service ID
        if (selectedServiceName != null && !"All Services".equals(selectedServiceName)) {
            Optional<Service> selectedServiceOpt = servicesList.stream()
                    .filter(service -> service.getName().equals(selectedServiceName))
                    .findFirst();

            if (selectedServiceOpt.isPresent()) {
                serviceId = selectedServiceOpt.get().getId();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Service Error", "Selected service not found.");
                return;
            }
        }

        // If "All Localisations" is not selected, find the corresponding localisation ID
        if (selectedLocalisationName != null && !"All Localisations".equals(selectedLocalisationName)) {
            Optional<Localisation> selectedLocalisationOpt = localisationsList.stream()
                    .filter(localisation -> localisation.getLocName().equals(selectedLocalisationName))
                    .findFirst();

            if (selectedLocalisationOpt.isPresent()) {
                localisationId = selectedLocalisationOpt.get().getId();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Localisation Error", "Selected localisation not found.");
                return;
            }
        }

        // Logic to extract the inventory report based on selected service, localisation, and year
        try {
            List<Inventaire_Item> inventoryItems = dbhelper.getInventoryItemsByFilters(serviceId, localisationId, selectedYear);

            if (inventoryItems.isEmpty()) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "No Data", "No inventory items found for the selected filters.");
                return; // Exit if no items found
            }

            // Generate the report
            generateReport(selectedYear, selectedServiceName, selectedLocalisationName, inventoryItems);

        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching the inventory data.");
        }
    }


    /**
     * private void generateReport(int year, String serviceName, String localisationName, List<Inventaire_Item> items) {
     * Workbook workbook = new XSSFWorkbook(); // Create a new Excel workbook
     * Sheet sheet = workbook.createSheet("Inventory Report"); // Create a new sheet in the workbook
     * <p>
     * // Create header row
     * Row headerRow = sheet.createRow(0);
     * headerRow.createCell(0).setCellValue("Code barre");
     * headerRow.createCell(1).setCellValue("Designation");
     * headerRow.createCell(2).setCellValue("N° Inventaire");
     * <p>
     * // Populate rows with data
     * int rowIndex = 1; // Start after the header row
     * for (Inventaire_Item item : items) {
     * Row row = sheet.createRow(rowIndex++);
     * row.createCell(0).setCellValue(item.getId());
     * row.createCell(1).setCellValue(item.getArticle_id());
     * row.createCell(2).setCellValue(item.getNum_inventaire());
     * }
     * <p>
     * // Adjust column widths for better readability
     * sheet.autoSizeColumn(0);
     * sheet.autoSizeColumn(1);
     * sheet.autoSizeColumn(2);
     * <p>
     * // Use FileChooser to let the user select where to save the Excel file
     * FileChooser fileChooser = new FileChooser();
     * fileChooser.setTitle("Save Excel Report");
     * fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
     * <p>
     * File file = fileChooser.showSaveDialog(new Stage());
     * <p>
     * if (file != null) {
     * try (FileOutputStream fileOut = new FileOutputStream(file)) {
     * workbook.write(fileOut); // Write workbook to the chosen file
     * workbook.close();
     * GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "The report has been saved successfully.");
     * } catch (IOException e) {
     * e.printStackTrace();
     * GeneralUtil.showAlert(Alert.AlertType.ERROR, "File Error", "An error occurred while saving the Excel file.");
     * }
     * } else {
     * GeneralUtil.showAlert(Alert.AlertType.WARNING, "Save Cancelled", "No file was selected.");
     * }
     * }
     */

    private void generateReport(int year, String serviceName, String localisationName, List<Inventaire_Item> items) {
        Workbook workbook = new XSSFWorkbook(); // Create a new Excel workbook
        Sheet sheet = workbook.createSheet("Inventory Report"); // Create a new sheet in the workbook

        // Set margins for the sheet
        setSheetMargins(sheet);

        // Create header row for the fixed header
        createHeaderRow(sheet, workbook);


        // logo
        createLogoRow(sheet, workbook);
        // Title
        createTitleRow(sheet, workbook);

        // Localisation
        createLocalisationRow(sheet, localisationName);

        // Create header row for the table
        createTableHeaderRow(sheet, workbook);

        // Populate rows with data
        populateDataRows(sheet, items, workbook);

        // Add total number of articles
        addTotalRow(sheet, items.size());

        // Add responsible person section
        addResponsiblePersonSection(sheet);

        // Use FileChooser to let the user select where to save the Excel file
        saveWorkbook(workbook);
    }

    private void setSheetMargins(Sheet sheet) {
        sheet.setMargin(Sheet.TopMargin, 0.75);
        sheet.setMargin(Sheet.BottomMargin, 0.75);
        sheet.setMargin(Sheet.LeftMargin, 0.75);
        sheet.setMargin(Sheet.RightMargin, 0.75);
    }

    private void createHeaderRow(Sheet sheet, Workbook workbook) {
        // Create the header row
        Row headerRow = sheet.createRow(0);

        // Create a cell and set its value
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("République Algérienne Démocratique et Populaire\nMinistère de la Justice\nEcole Supérieure de la Magistrature");

        // Set the cell style
        headerCell.setCellStyle(createHeaderCellStyle(workbook));

        // Merge cells from A1 to C1
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        // Adjust the row height to fit the content
        headerRow.setHeightInPoints(60); // Adjust the height as needed

        // Set column widths for columns A to C to fit the content
        sheet.setColumnWidth(0, 256 * 30); // Column A width
        sheet.setColumnWidth(1, 256 * 30); // Column B width
        sheet.setColumnWidth(2, 256 * 30); // Column C width
    }

    private void createLogoRow(Sheet sheet, Workbook workbook) {
        System.out.println("Adding logo...");

        // Load the image file
        try (InputStream is = this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")) {

            // Ensure the InputStream is not null
            if (is == null) {
                System.err.println("Logo file not found.");
                return;
            }

            // Read and add image to workbook
            byte[] inputImageBytes1 = IOUtils.toByteArray(is);
            int inputImagePictureID1 = workbook.addPicture(inputImageBytes1, Workbook.PICTURE_TYPE_PNG);

            // Add logo drawing
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor();

            // Set height for the row containing the logo
            Row logoRow = sheet.getRow(1); // Row index 1 corresponds to row 2 in Excel
            if (logoRow == null) {
                logoRow = sheet.createRow(1); // Create the row if it doesn't exist
            }
            logoRow.setHeightInPoints(50); // Set the height (adjust as needed)

            // Set anchor for the image
            anchor.setRow1(1);  // Start from row 2 (Excel rows are zero-indexed)
            anchor.setRow2(2);  // End at row 2 (image will fit in 1 row)

            // Define the width of the image (in pixels)
            int imageWidthInPixels = 10; // Set your desired width here

            // Convert pixel width to Excel column width (1 character width = 256 pixels)
            int columnWidthInUnits = (int) Math.round(imageWidthInPixels / 7.5); // Approximate conversion
            int startCol = 1; // Starting column (C)
            int endCol = startCol + columnWidthInUnits; // Calculate end column based on width

            // Adjust anchor columns for centering the image
            anchor.setCol1(startCol);  // Start at column C
            anchor.setCol2(endCol);     // End at the calculated column

            // Center the image horizontally within the merged region
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE); // Prevent resizing on row/column changes
            drawing.createPicture(anchor, inputImagePictureID1);
            // Set fixed column widths for better control
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            // Create new CellRangeAddress for the merged region
            CellRangeAddress newRegion = new CellRangeAddress(1, 1, 0, 2); // A2:D2
            // Check for overlapping with existing merged regions
            if (!isOverlappingMergedRegion(sheet, newRegion)) {
                sheet.addMergedRegion(newRegion); // Merging A2:D2 for logo area
            } else {
                System.err.println("Merged region A2:D2 overlaps with an existing region.");
            }
        } catch (IOException e) {
            System.err.println("Error adding logo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isOverlappingMergedRegion(Sheet sheet, CellRangeAddress newRegion) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress existingRegion = sheet.getMergedRegion(i);
            if (existingRegion.intersects(newRegion)) { // Check intersection with new region
                return true;
            }
        }
        return false;
    }


    private void createTitleRow(Sheet sheet, Workbook workbook) {
        Row titleRow = sheet.createRow(3);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Fiche d'Inventaire");
        titleCell.setCellStyle(createTitleCellStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2)); // Merging title across columns A to C
    }

    private void createLocalisationRow(Sheet sheet, String localisationName) {
        Row localisationRow = sheet.createRow(4);
        localisationRow.createCell(0).setCellValue("Localisation: " + (localisationName != null ? localisationName : "tout Localisations"));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2)); // Merging localisation across columns A to C
    }

    private void createTableHeaderRow(Sheet sheet, Workbook workbook) {
        Row tableHeaderRow = sheet.createRow(5);
        String[] headers = {"Code barre", "Designation", "N° Inventaire"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = tableHeaderRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createTableHeaderCellStyle(workbook));
            sheet.setColumnWidth(i, 4000); // Set fixed column width
        }
    }

    private void populateDataRows(Sheet sheet, List<Inventaire_Item> items, Workbook workbook) {
        int rowIndex = 6; // Start after the table header row
        for (Inventaire_Item item : items) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(item.getId());
            String articlename = dbhelper.getArticleById(item.getArticle_id()).getName();
            row.createCell(1).setCellValue(articlename != null ? articlename : "not known article");
            row.createCell(2).setCellValue(item.getNum_inventaire());

            // Adjust column widths for better readabilit
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            for (int i = 0; i < 3; i++) {
                Cell cell = row.getCell(i);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setWrapText(true);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(cellStyle);

            }
        }
    }

    private void addTotalRow(Sheet sheet, int totalCount) {
        int rowIndex = sheet.getLastRowNum() + 1;
        Row totalRow = sheet.createRow(rowIndex);
        totalRow.createCell(0).setCellValue("Total Number of Articles: " + totalCount);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2)); // Merging total across columns A to C
    }

    private void addResponsiblePersonSection(Sheet sheet) {
        int rowIndex = sheet.getLastRowNum() + 1;
        Row responsibleRow = sheet.createRow(rowIndex);
        responsibleRow.createCell(0).setCellValue("Le Responsable du Bureau:");
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2)); // Merging responsible across columns A to C
        String[] labels = {"NOM:", "PRENOM:", "FONCTION:"};
        for (int i = 0; i < labels.length; i++) {
            Row labelRow = sheet.createRow(rowIndex + 1 + i);
            labelRow.createCell(0).setCellValue(labels[i]);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex + 1 + i, rowIndex + 1 + i, 0, 2)); // Merging label across columns A to C
        }
    }

    private void saveWorkbook(Workbook workbook) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "The report has been saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "File Error", "An error occurred while saving the Excel file.");
            } finally {
                try {
                    workbook.close(); // Ensure the workbook is closed
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "Save Cancelled", "No file was selected.");
        }
    }


    // Method to create a cell style for the header
    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12); // Reduced font size
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    // Method to create a cell style for the title
    private CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // Reduced font size
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    // Method to create a cell style for table headers
    private CellStyle createTableHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12); // Reduced font size
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }


    // Navigate back to the dashboard
    @FXML
    public void goBackDashboard(ActionEvent event) {
        bk_Dashboard_from_fiche_inventaire.setOnAction(GeneralUtil::goBackDashboard);
    }
}