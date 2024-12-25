package com.marrok.inventaire_esm.controller.inventaire;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.database.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DetailController {
    Logger logger = LogManager.getLogger(DetailController.class);
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
    Map<String, Object> parameters = new HashMap<>();

    private Inventaire_Item inventaireItem;
    private Stage stage;
    private UserDbHelper udbhelper;
    private ArticleDbHelper articleDbhelper;
    private InventaireItemDbHelper inventaireItemDbhelper;
    private EmployerDbHelper employerDbHelper;
    LocDbhelper locDbhelper ;
    String userName = "", employerName = "", locationName = "", articleName="";

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInventaireDetails(Inventaire_Item selectedInventaire) throws SQLException {
        logger.info("setInventaireDetails");
        this.inventaireItem = selectedInventaire;
        udbhelper = new UserDbHelper();
        articleDbhelper=new ArticleDbHelper();
        locDbhelper = new LocDbhelper();
        inventaireItemDbhelper = new InventaireItemDbHelper();
        employerDbHelper = new EmployerDbHelper();

        if (inventaireItem != null) {
            Article currentarticle = articleDbhelper.getArticleById(inventaireItem.getArticle_id());
            Inventaire_Item currentInventaireItem = inventaireItemDbhelper.getInevntaireItemById(inventaireItem.getId());
            logger.info(currentarticle + " " + currentInventaireItem);
            if (currentarticle != null && currentInventaireItem != null) {
                numInventaireLabel.setText(selectedInventaire.getNum_inventaire());
                idLabel.setText(String.valueOf(currentInventaireItem.getId()));
                articleName = currentarticle.getName();
                idArticleLabel.setText(articleName);
                employerName = employerDbHelper.getEmployerFullNameById(currentInventaireItem.getEmployer_id());
                idEmployerLabel.setText(employerName);
                Localisation l= locDbhelper.getLocalisationById(currentInventaireItem.getLocalisation_id());
                locationName = "الطابق: " + l.getFloor() + "   " + l.getLocName();//dont change space until
                idLocalisationLabel.setText(locationName);
                timeLabel.setText(selectedInventaire.getFormattedDateTime());
                userName = udbhelper.getUserNameById(currentInventaireItem.getUser_id());
                inventaire_status.setText(currentInventaireItem.getStatus());
                userIdLabel.setText(userName);
            } else {
              logger.info(" else currentarticle != null && currentInventaireItem != null");
            }

            BufferedImage barcodeImage = null;
            try {
                barcodeImage = generateBarcode(inventaireItem.getNum_inventaire());
                parameters.put("img_bar_code", barcodeImage);
                // Convert BufferedImage to JavaFX Image
                Image fxImage = SwingFXUtils.toFXImage(barcodeImage, null);
                // Set the image to ImageView in FXML
                barcodeImageView.setImage(fxImage);
            } catch (WriterException e) {
              logger.error(e);
            }

        }
    }

    public void printDetInvetaire(ActionEvent actionEvent) {
        logger.info("printDetInvetaire");
        Connection connection = null;
        try {
            // Load the Jasper report
            connection = DatabaseConnection.getInstance().getConnection();
            Integer inventaireItemId =inventaireItem.getId();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/InventaireDetails.jrxml");
            if (reportStream == null) {
                logger.error("Report file not found.");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            // Create parameters map
            parameters.put("inventaireItemId", inventaireItemId);

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection); // Use your own method to get database connection

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("تفاصيل الجرد");
            viewer.setVisible(true);

        } catch (SQLException sqlEx) {
            logger.error("SQL Error: " + sqlEx.getMessage());
        } catch (Exception ex) {
            logger.error("Error generating report: " + ex.getMessage());
        }
    }


    private BufferedImage generateBarcode(String text) throws WriterException {
        logger.info("generateBarcode");
        int width = 400;
        int height = 200;
        BitMatrix bitMatrix = new com.google.zxing.MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
