package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class DetailController {

    private Article article;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label unitLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label remarkLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label descriptionLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setArticleDetails(Article detailedArticle) {
        this.article = detailedArticle;
        if (article != null) {
            nameLabel.setText(article.getName());
            idLabel.setText(String.valueOf(article.getId()));
            unitLabel.setText(article.getUnite());
            quantityLabel.setText(String.valueOf(article.getQuantity()));
            remarkLabel.setText(article.getRemarque());
            categoryLabel.setText(String.valueOf(article.getIdCategory()));
            descriptionLabel.setText(article.getDescription());
        }
    }


    public void printDetArticle(ActionEvent actionEvent) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getInstance().getConnection();
            Integer articleId = article.getId();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/article_Details.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Report file not found.");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("article_id", articleId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection); // Use your own method to get database connection

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("تفاصيل العنصر");
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

}
