package com.marrok.inventaire_esm.controller.article;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.marrok.inventaire_esm.model.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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





    @FXML
    public void printDetArticle(ActionEvent actionEvent) {
        if (article != null) {
            try (PDDocument document = new PDDocument()) {
                // Load Arabic font with PDFBox for correct text direction handling
                PDFont font = PDType0Font.load(document, getClass().getResourceAsStream("/com/marrok/inventaire_esm/fonts/Amiri.ttf"));

                // Define margins and leading
                float marginX = 50;
                float marginY = 50;
                float leading = 14.5f; // Line spacing

                // Details content
                String content = String.format("""
                        المعرف: %s
                        اسم العنصر: %s
                        الوحدة: %s
                        الكمية: %s
                        ملاحظة: %s
                        الفئة: %s
                        الوصف: %s
                        تفاصيل العنصر
                        """,
                        article.getId(), article.getName(), article.getUnite(),
                        article.getQuantity(), article.getRemarque(), article.getIdCategory(), article.getDescription());

                content = bidiReorder(content);
                String[] lines = content.split("\n");

                // Calculate required page dimensions
                float maxWidth = 0;
                for (String line : lines) {
                    float width = font.getStringWidth(line) / 1000 * 12; // Convert from font units to user space units
                    if (width > maxWidth) {
                        maxWidth = width;
                    }
                }

                float textWidth = maxWidth + marginX * 2;
                float textHeight = lines.length * leading + marginY * 2;

                // Create a page with custom size
                PDRectangle pageSize = new PDRectangle(textWidth, textHeight);
                PDPage page = new PDPage(pageSize);
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(pageSize.getWidth() - marginX, textHeight - marginY - 12); // Start at top-right corner

                    for (String line : lines) {
                        float lineWidth = font.getStringWidth(line) / 1000 * 12;
                        contentStream.newLineAtOffset(-lineWidth, 0); // Adjust for right alignment
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(lineWidth, -leading); // Reset for the next line
                    }

                    contentStream.endText();
                }

                // Save and open the PDF file
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                fileChooser.setInitialFileName("ArticleDetails_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    document.save(file);
                    Desktop.getDesktop().open(file);
                }
            } catch (IOException | ArabicShapingException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }


    private String bidiReorder(String text) throws ArabicShapingException {
        Bidi bidi = new Bidi(new ArabicShaping(ArabicShaping.LETTERS_SHAPE).shape(text), Bidi.DIRECTION_RIGHT_TO_LEFT);
        bidi.setReorderingMode(Bidi.REORDER_RUNS_ONLY);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }



}
