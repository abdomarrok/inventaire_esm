package com.marrok.inventaire_esm.controller.article;


import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.CategoryDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ArticleController implements Initializable {
    Logger logger = Logger.getLogger(ArticleController.class);

    @FXML
    public TableView<Article> tableView;

    @FXML
    public TableColumn<Article, Integer> id_article_v;
    @FXML
    public TableColumn<Article, String> nameColumn;
    @FXML
    public TableColumn<Article, String> unitColumn;

    @FXML
    public TableColumn<Article, String> remarkColumn;
    @FXML
    public TableColumn descriptionColumn;
    @FXML
    public TableColumn<Article, String> categoryColmun;
    @FXML
    public TextField searchField;

    @FXML
    public Button addButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;

    private ObservableList<Article> articleList;
    private FilteredList<Article> filteredArticleList;

    private Article selectedArticle;
    @FXML
    private Label titleLabel;

    @FXML
    public Button bk_Dashboard_from_products;

    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private CategoryDbHelper categoryDbhelper = new CategoryDbHelper();
    private Map<Integer, String> categoryMap;

    public ArticleController() throws SQLException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing ArticleController");
        preloadCategories();
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {
        logger.info("Initializing columns");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        // Set cell value factories
        id_article_v.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Custom cell value factory for categoryColmun to fetch category name by id_category
        categoryColmun.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getIdCategory();
            String categoryName = categoryDbhelper.getCategoryById(categoryId);
            return new SimpleStringProperty(categoryName != null && !categoryName.isEmpty() ? categoryName : "Unknown Category");
        });
    }






    public void loadData() {
            logger.info("Loading data");
            List<Article> articles = articleDbhelper.getArticles();
            articleList = FXCollections.observableArrayList(articles);
            filteredArticleList = new FilteredList<>(articleList, p -> true);
            tableView.setItems(filteredArticleList);

    }

    private void preloadCategories() {
        logger.info("preloade");
        categoryMap = categoryDbhelper.getCategories().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredArticleList.setPredicate(article -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String categoryName = categoryMap.getOrDefault(article.getIdCategory(), "Unknown Category");
                String lowerCaseFilter = newValue.toLowerCase();

                return article.getName().toLowerCase().contains(lowerCaseFilter)
                        || article.getUnite().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(article.getId()).contains(lowerCaseFilter)
                        || categoryName.toLowerCase().contains(lowerCaseFilter);
            });
        });
    }


    private void setupTableSelectionListener() {
        bk_Dashboard_from_products.setOnAction(event -> {
           GeneralUtil.goBackStockDashboard(event);
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedArticle = newValue);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2 && tableView.getSelectionModel().getSelectedItem() != null) {
                Article selectedArticle = tableView.getSelectionModel().getSelectedItem();
                logger.info("Selected article: " + selectedArticle);
            }
        });
    }

    @FXML
    public void addArticle(ActionEvent event) {
        logger.info("Adding article");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/article/add_form-view.fxml"));
        try {

            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setResizable(false); // Ensure the stage is non-resizable
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.setTitle("Add Article");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));

            AddController controller = loader.getController();
            controller.setDashboardController(this);
            stage.showAndWait();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة العنصر.");
            e.printStackTrace();
        }
    }


    @FXML
    public void updateArticle(ActionEvent event) {
        logger.info("Updating article");
        Article selectedArticle = tableView.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/article/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("تحديث العنصر");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                // Get the controller of the update form
                UpdateController controller = loader.getController();
                controller.setArticleData(selectedArticle.getId());
                controller.setDashboardController(this);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار عنصر للتحديث.");

        }
    }

    @FXML
    public void deleteArticle(ActionEvent event) {
        logger.info("Deleting article");
        Article selectedArticle = tableView.getSelectionModel().getSelectedItem();

        if (selectedArticle != null) {

            boolean test = GeneralUtil.showConfirmationDialog("تاكيد", "هل متاكد انك تريد حذف" + selectedArticle.getName());
            if (test) {
                try {
                    if (articleDbhelper.deleteArticle(selectedArticle.getId())) {
                        articleList.remove(selectedArticle);
                        GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم حذف العنصر", "تم حذف العنصر بنجاح.");

                    }else{
                        GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في حذف العنصر", "");
                    }
                } catch (Exception e) {
                    GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في حذف العنصر", e.getMessage());
                }
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار عنصر للحذف.");

        }
    }

    public ObservableList<Article> getArticleList() {
        return articleList;
    }

    public void refreshTableData() {
            List<Article> articles = articleDbhelper.getArticles();
            articleList.setAll(articles);
            tableView.refresh();
    }


    @FXML
    private void exportArticle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("حفظ العناصر");
        fileChooser.setInitialFileName("articles.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "name", "unite", "description", "remarque", "id_category"))) {

                for (Article article : articleList) {
                    csvPrinter.printRecord(article.getId(), article.getName(), article.getUnite(), article.getDescription(),
                            article.getRemarque(), article.getIdCategory());
                }
                csvPrinter.flush();
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم التصدير بنجاح", "تم تصدير العناصر بنجاح.");

            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التصدير", "حدث خطأ أثناء تصدير العناصر.");
                logger.error(e);
            }
        }
    }

    @FXML
    private void importArticle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Articles");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                CSVParser parser = CSVFormat.DEFAULT
                        .withHeader("id", "name", "unite", "description", "remarque", "id_category")
                        .withFirstRecordAsHeader()
                        .parse(reader);

                List<Article> articlesToAdd = new ArrayList<>();

                for (CSVRecord record : parser) {
                    try {
                        String name = record.get("name");
                        String unite = record.get("unite");
                        String description = record.get("description");
                        String remarque = record.get("remarque");
                        Integer idCategory = record.get("id_category") != null ? Integer.parseInt(record.get("id_category")) : null;

                        if (name != null && !name.isEmpty()) {
                            Article article = new Article(name, unite, description, remarque, idCategory);
                            articlesToAdd.add(article);
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Invalid id_category value in CSV: " + e);
                    }
                }

                articleDbhelper.addArticles(articlesToAdd);
                articleList.addAll(articlesToAdd);
                loadData();
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم الاستيراد بنجاح", "تم استيراد العناصر بنجاح.");

            } catch (IOException | SQLException e) {
                logger.error(e);
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل الاستيراد", "حدث خطأ أثناء استيراد العناصر.");

            }
        }
    }


}
