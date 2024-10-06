package com.marrok.inventaire_esm.controller.inventaire;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.DatabaseConnection;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DetailController {
    public Label inventaire_status;
    @FXML
    private Label numInventaireLabel;
    @FXML
    private Label idArticleLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label idEmployerLabel;
    @FXML
    private Label idLocalisationLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private ImageView barcodeImageView;

    private Inventaire_Item inventaireItem;
    private Stage stage;
    private DatabaseHelper dbhelper;
    String userName = "", employerName = "", locationName = "", articleName="";

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInventaireDetails(Inventaire_Item selectedInventaire) throws SQLException {
        this.inventaireItem = selectedInventaire;
        dbhelper = new DatabaseHelper();

        if (inventaireItem != null) {

            Article currentarticle = dbhelper.getArticleById(inventaireItem.getArticle_id());
            Inventaire_Item currentInventaireItem = dbhelper.getInevntaireItemById(inventaireItem.getId());
            System.out.println(currentarticle + " " + currentInventaireItem);
            if (currentarticle != null && currentInventaireItem != null) {
                numInventaireLabel.setText(selectedInventaire.getNum_inventaire());
                idLabel.setText(String.valueOf(currentInventaireItem.getId()));
                articleName = currentarticle.getName();
                idArticleLabel.setText(articleName);
                employerName = dbhelper.getEmployerFullNameById(currentInventaireItem.getEmployer_id());
                idEmployerLabel.setText(employerName);
                Localisation l= dbhelper.getLocalisationById(currentInventaireItem.getLocalisation_id());
                locationName = "الطابق: " + l.getFloor() + "   " + l.getLocName();//dont change space until
                idLocalisationLabel.setText(locationName);
                timeLabel.setText(selectedInventaire.getFormattedDateTime());
                userName = dbhelper.getUserNameById(currentInventaireItem.getUser_id());
                inventaire_status.setText(currentInventaireItem.getStatus());
                userIdLabel.setText(userName);
            } else {
                System.out.println(" else currentarticle != null && currentInventaireItem != null");
            }


            BufferedImage barcodeImage = null;
            try {
                barcodeImage = generateBarcode(inventaireItem.getNum_inventaire());
                // Convert BufferedImage to JavaFX Image
                Image fxImage = SwingFXUtils.toFXImage(barcodeImage, null);

                // Set the image to ImageView in FXML
                barcodeImageView.setImage(fxImage);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }


        }
    }

    public void printDetInvetaire(ActionEvent actionEvent) {
        Connection connection = null;
        try {
            // Load the Jasper report
            connection = DatabaseConnection.getInstance().getConnection();
            Integer inventaireItemId =inventaireItem.getId();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/InventaireDetails.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Report file not found.");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            // Create parameters map
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("inventaireItemId", inventaireItemId);

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection); // Use your own method to get database connection

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

        } catch (FileNotFoundException fnf) {
            System.out.println("Report file not found: " + fnf.getMessage());
            fnf.printStackTrace();
        } catch (SQLException sqlEx) {
            System.out.println("SQL Error: " + sqlEx.getMessage());
            sqlEx.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error generating report: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
//    @FXML
//    public void printDetInvetaire(ActionEvent actionEvent) {
//        if (inventaireItem != null) {
//            try (PDDocument document = new PDDocument()) {
//                // Load Arabic font with PDFBox for correct text direction handling
//                PDFont font = PDType0Font.load(document, getClass().getResourceAsStream("/com/marrok/inventaire_esm/fonts/Amiri.ttf"));
//
//                // Define margins and leading
//                float marginX = 50;
//                float marginY = 50;
//                float leading = 14.5f; // Line spacing
//
//                // Details content
//                String content = String.format("""
//                            معرف الجرد:    %s
//
//                            العنصر:        %s
//
//                            الموقع:        %s
//
//                            المستخدم:      %s
//
//                            الوقت:         %s
//
//                            رقم الجرد:     %s
//
//                            الموظف:        %s
//                            """,
//                        inventaireItem.getId(), articleName, locationName,
//                        userName, inventaireItem.getFormattedDateTime(), inventaireItem.getNum_inventaire(),
//                        employerName);
//
//                content = bidiReorder(content);
//                String[] lines = content.split("\n");
//
//                // Calculate required page dimensions
//                float maxWidth = 0;
//                for (String line : lines) {
//                    float width = font.getStringWidth(line) / 1000 * 14; // Convert from font units to user space units
//                    if (width > maxWidth) {
//                        maxWidth = width;
//                    }
//                }
//
//                float textWidth = maxWidth + marginX * 2;
//                float textHeight = lines.length * leading + marginY * 2 + 120; // Extra space for barcode
//
//                // Create a page with custom size
//                PDRectangle pageSize = new PDRectangle(textWidth, textHeight);
//                PDPage page = new PDPage(pageSize);
//                document.addPage(page);
//
//                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                    contentStream.beginText();
//                    contentStream.setFont(font, 14);
//                    contentStream.newLineAtOffset(pageSize.getWidth() - marginX, textHeight - marginY - 12); // Start at top-right corner
//
//                    for (String line : lines) {
//                        contentStream.newLineAtOffset(-font.getStringWidth(line) / 1000 * 14, 0); // Adjust for right alignment
//                        contentStream.showText(line);
//                        contentStream.newLineAtOffset(font.getStringWidth(line) / 1000 * 14, -leading); // Reset for the next line
//                    }
//
//                    contentStream.endText();
//
//                    // Generate barcode
//                    BufferedImage barcodeImage = generateBarcode(inventaireItem.getNum_inventaire());
//
//                    // Save barcode image to temporary file
//                    Path tempFilePath = Files.createTempFile("barcode", ".png");
//                    ImageIO.write(barcodeImage, "png", tempFilePath.toFile());
//
//                    // Draw barcode on PDF
//                    PDImageXObject barcode = PDImageXObject.createFromFile(tempFilePath.toString(), document);
//                    float barcodeWidth = 200;
//                    float barcodeHeight = 100;
//                    float xPosition = (pageSize.getWidth() - barcodeWidth) / 2;
//                    float yPosition = marginY;
//                    contentStream.drawImage(barcode, xPosition, yPosition, barcodeWidth, barcodeHeight);
//
//                    // Delete temporary file
//                    Files.delete(tempFilePath);
//                }
//
//                // Save and open the PDF file
//                FileChooser fileChooser = new FileChooser();
//                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
//                fileChooser.setInitialFileName("InventaireDetails_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
//                File file = fileChooser.showSaveDialog(stage);
//                if (file != null) {
//                    document.save(file);
//                    Desktop.getDesktop().open(file);
//                }
//            } catch (IOException | ArabicShapingException | WriterException e) {
//                e.printStackTrace();
//                // Handle exception
//            }
//        }
//    }

  

    private BufferedImage generateBarcode(String text) throws WriterException {
        int width = 400;
        int height = 200;
        BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
