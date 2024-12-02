package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.StockAdjustment;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ArticleDbHelper {
    Logger logger = Logger.getLogger(ArticleDbHelper.class);
     public   ArticleDbHelper()  throws SQLException {
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public Connection cnn;
    public List<String> getAllArticlesNames() {
        logger.info("getAllArticlesNames");
        List<String> articles = new ArrayList<>();
        String query = "SELECT name FROM article ORDER BY name DESC;";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                articles.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            logger.error("Error getting  articles ", e);
        }
        return articles;
    }

    // Method to fetch all articles
    public List<Article> getArticles() {
        logger.info("getArticles");
        List<Article> articles = new ArrayList<>();
        String query = "SELECT id, name, unite, remarque, description, id_category, last_edited,min_quantity FROM article ORDER BY last_edited DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unite"),
                        rs.getString("remarque"),
                        rs.getString("description"),
                        rs.getInt("id_category"),
                        rs.getInt("min_quantity")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            logger.error("Error getting  articles ", e);

        }
        return articles;
    }

    public Article getArticleById(long id) {
        logger.info("getArticleById");
        String query = "SELECT * FROM article WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Article(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("unite"),
                            rs.getString("remarque"),
                            rs.getString("description"),
                            rs.getInt("id_category"),
                            rs.getInt("min_quantity")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting  article with id "+id, e);

        }
        return null; // Return null if article is not found
    }

    public boolean addArticle(Article article) {
        logger.info("addArticle");
        String query = "INSERT INTO article (name, unite,  remarque, description, id_category,min_quantity) VALUES (?, ?, ?,  ?, ?,?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setString(3, article.getRemarque());
            stmt.setString(4, article.getDescription());
            stmt.setInt(5, article.getIdCategory());
            stmt.setInt(6, article.getMin_quantity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error("Error adding article", e);
            // Handle exception
        }
        return false;
    }

    public void addArticles(List<Article> articles) throws SQLException {
        logger.info("addArticles");
        for (Article article : articles) {
            addArticle(article);
        }
    }



    public boolean updateArticle(Article article) {
        logger.info("updateArticle");
        String query = "UPDATE article SET name = ?, unite = ?, remarque = ?, description = ?, id_category = ?  ,min_quantity=? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setString(3, article.getRemarque());
            stmt.setString(4, article.getDescription());
            stmt.setInt(5, article.getIdCategory());
            stmt.setInt(6, article.getMin_quantity());
            stmt.setLong(7, article.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error( "Error updating article "+article.getName(), e);

        }
        return false;
    }

    public boolean deleteArticle(long id) {
        logger.info("deleteArticle");
        String query = "DELETE FROM article WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {

            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لا تستطيع حذف هذا العنصر لانه مازال مرفقا بجرد او وصل استلام");
        } catch (SQLException e) {
            logger.error( "Error deleting  article with id "+id, e);
        }
        return false;
    }
    // Method to fetch article ID by name
    public  int getArticleIdByName(String name) {
        logger.info("getArticleIdByName");
        String query = "SELECT id FROM article WHERE name = ?";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            logger.error( "Error getting article id by its name: "+name, e);
        }
        return -1; // Return -1 if not found
    }
    public int getTotalQuantityByArticleId(int articleId) {
        logger.info("getTotalQuantityByArticleId");

        int totalEntree = getTotalEntredQuantityByArticleId(articleId);
        int totalSortie = getTotalSortieQuantityByArticleId(articleId);
        int totalRetour = getTotalRetourQuantityByArticleId(articleId);
        int totalAdjustment = getTotalAdjustmentByArticleId(articleId);

        // Net quantity = Entries - Exits + Returns + Adjustments
        return totalEntree - totalSortie + totalRetour + totalAdjustment;
    }

    public int getTotalEntredQuantityByArticleId(int articleId) {
        logger.info("getTotalEntredQuantityByArticleId");
        String query = "SELECT COALESCE(SUM(quantity), 0) AS total_entree FROM entree WHERE id_article = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_entree");
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting Total Entrée Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no entries are found
    }
    public int getTotalSortieQuantityByArticleId(int articleId) {
        logger.info("getTotalSortieQuantityByArticleId");
        String query = "SELECT COALESCE(SUM(quantity), 0) AS total_sortie FROM sortie WHERE id_article = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_sortie");
                }
            }
        } catch (SQLException e) {
            logger.error(
                    "Error getting Total Sortie Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no records are found
    }
    public int getTotalRetourQuantityByArticleId(int articleId) {
        logger.info("getTotalRetourQuantityByArticleId");

        String query = "SELECT COALESCE(SUM(quantity), 0) AS total_retour FROM retour WHERE id_article = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_retour");
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting Total Retour Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no returns are found
    }

    public Map<Integer, Integer> getTotalQuantitiesByArticle() {
        logger.info("getTotalQuantitiesByArticle");
        Map<Integer, Integer> totalQuantities = new HashMap<>();

        String query = "SELECT article.id AS article_id, " +
                "   (SELECT COALESCE(SUM(entree.quantity), 0) FROM entree WHERE entree.id_article = article.id) AS total_entree, " +
                "   (SELECT COALESCE(SUM(sortie.quantity), 0) FROM sortie WHERE sortie.id_article = article.id) AS total_sortie, " +
                "   (SELECT COALESCE(SUM(retour.quantity), 0) FROM retour WHERE retour.id_article = article.id) AS total_retour, " +
                "   (SELECT COALESCE(SUM(CASE WHEN adjustment_type = 'increase' THEN quantity ELSE 0 END), 0) - " +
                "       COALESCE(SUM(CASE WHEN adjustment_type = 'decrease' THEN quantity ELSE 0 END), 0) " +
                "    FROM stock_adjustment WHERE article_id = article.id) AS net_adjustment " +
                "FROM article " +
                "ORDER BY (total_entree - total_sortie + total_retour + net_adjustment) DESC, article.id ASC";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("article_id");  // Use alias 'article_id'
                int totalQuantity = resultSet.getInt("total_entree")
                        - resultSet.getInt("total_sortie")
                        + resultSet.getInt("total_retour")
                        + resultSet.getInt("net_adjustment");
                totalQuantities.put(articleId, totalQuantity);
            }
        } catch (SQLException e) {
            logger.error("Error getting Total Quantities By Article", e);
        }

        return totalQuantities;
    }

    public Map<Integer, Integer> getMinimalQuantitiesByArticle() {
        logger.info("getMinimalQuantitiesByArticle");
        Map<Integer, Integer> minimal_Quantities = new HashMap<>();
        String query = "SELECT article.id AS article_id, min_quantity from article ORDER BY article.id ASC;";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("article_id");  // Use alias 'article_id'
                int min_Quantity = resultSet.getInt("min_quantity");
                minimal_Quantities.put(articleId, min_Quantity);
            }
        } catch (SQLException e) {
            logger.error("Error getting Total Quantities By Article", e);
        }

        return minimal_Quantities;
    }


    public int getTotalAdjustmentByArticleId(int articleId) {
        logger.info("getTotalAdjustmentByArticleId");
        String query = "SELECT " +
                "SUM(CASE WHEN adjustment_type = 'increase' THEN quantity ELSE 0 END) - " +
                "SUM(CASE WHEN adjustment_type = 'decrease' THEN quantity ELSE 0 END) AS net_adjustment " +
                "FROM stock_adjustment " +
                "WHERE article_id = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("net_adjustment");
                }
            }
        } catch (SQLException e) {
            logger.error(
                    "Error getting Total Adjustment Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no adjustments are found
    }

    public Map<Integer, Integer> getTotalAdjustments() {
        Map<Integer, Integer> adjustments = new HashMap<>();
        String sql = "SELECT article_id, " +
                "SUM(CASE WHEN adjustment_type = 'increase' THEN quantity ELSE 0 END) - " +
                "SUM(CASE WHEN adjustment_type = 'decrease' THEN quantity ELSE 0 END) AS net_adjustment " +
                "FROM stock_adjustment " +
                "GROUP BY article_id";

        try (Statement stmt = cnn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int articleId = rs.getInt("article_id");
                int netAdjustment = rs.getInt("net_adjustment");
                adjustments.put(articleId, netAdjustment);
            }
        } catch (SQLException e) {
            logger.error("Error getting total adjustments", e);
        }

        return adjustments;
    }



    // READ: Get a single stock adjustment by ID
    public StockAdjustment getStockAdjustmentById(int id) {
        logger.info("getStockAdjustmentById");
        String query = "SELECT * FROM stock_adjustment WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new StockAdjustment(
                        rs.getInt("id"),
                        rs.getInt("article_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("adjustment_date"),
                        rs.getInt("quantity"),
                        rs.getString("adjustment_type"),
                        rs.getString("remarks")
                );
            }
        } catch (SQLException e) {
            logger.error( "Error getting stock adjustment by ID", e);
        }
        return null;
    }

    // READ: Get all stock adjustments
    public List<StockAdjustment> getAllStockAdjustments() {
        logger.info("getAllStockAdjustments");
        List<StockAdjustment> adjustments = new ArrayList<>();
        String query = "SELECT * FROM stock_adjustment ORDER BY adjustment_date DESC";
        try (PreparedStatement stmt = cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StockAdjustment adjustment = new StockAdjustment(
                        rs.getInt("id"),
                        rs.getInt("article_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("adjustment_date"),
                        rs.getInt("quantity"),
                        rs.getString("adjustment_type"),
                        rs.getString("remarks")
                );
                adjustments.add(adjustment);
            }
        } catch (SQLException e) {
            logger.error("Error getting stock adjustments", e);
        }
        return adjustments;
    }


    // DELETE: Remove a stock adjustment by ID
    public boolean deleteStockAdjustment(int id) {
        logger.info("deleteStockAdjustment");
        String query = "DELETE FROM stock_adjustment WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting stock adjustment", e);
            return false;
        }
    }

    // CREATE: Add a new stock adjustment
    public boolean addStockAdjustment(StockAdjustment adjustment) {
        logger.info("addStockAdjustment");
        String query = "INSERT INTO stock_adjustment (article_id, user_id, adjustment_date, quantity, adjustment_type, remarks) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, adjustment.getArticleId());
            stmt.setInt(2, adjustment.getUserId());
            stmt.setDate(3,  new java.sql.Date(adjustment.getAdjustmentDate().getTime()));
            stmt.setInt(4, adjustment.getQuantity());
            stmt.setString(5, adjustment.getAdjustmentType());
            stmt.setString(6, adjustment.getRemarks());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding stock adjustment", e);
            return false;
        }
    }

    // UPDATE: Modify an existing stock adjustment
    public boolean updateStockAdjustment(StockAdjustment adjustment) {
        logger.info("updateStockAdjustment");
        String query = "UPDATE stock_adjustment SET article_id = ?, user_id = ?, adjustment_date = ?, quantity = ?, adjustment_type = ?, remarks = ? WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, adjustment.getArticleId());
            stmt.setInt(2, adjustment.getUserId());
            stmt.setDate(3, new java.sql.Date(adjustment.getAdjustmentDate().getTime()));
            stmt.setInt(4, adjustment.getQuantity());
            stmt.setString(5, adjustment.getAdjustmentType());
            stmt.setString(6, adjustment.getRemarks());
            stmt.setInt(7, adjustment.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error( "Error updating stock adjustment", e);
            return false;
        }
    }

    // Get the total entered quantities
    public Map<Integer, Integer> getTotalEntredQuantities() throws SQLException {
        logger.info("getTotalEntredQuantities");
        Map<Integer, Integer> enteredQuantities = new HashMap<>();
        String sql = "SELECT id_article, SUM(quantity) AS total_quantity FROM entree GROUP BY id_article";

        try (Statement stmt = cnn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int articleId = rs.getInt("id_article");
                int totalQuantity = rs.getInt("total_quantity");
                enteredQuantities.put(articleId, totalQuantity);
            }
        }
        return enteredQuantities;
    }

    // Get the total sortie (exit) quantities
    public Map<Integer, Integer> getTotalSortieQuantities() throws SQLException {
        Map<Integer, Integer> sortieQuantities = new HashMap<>();
        String sql = "SELECT id_article, SUM(quantity) AS total_quantity FROM sortie GROUP BY id_article";

        try (Statement stmt = cnn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int articleId = rs.getInt("id_article");
                int totalQuantity = rs.getInt("total_quantity");
                sortieQuantities.put(articleId, totalQuantity);
            }
        }
        return sortieQuantities;
    }
    // Get the total retour (return) quantities
    public Map<Integer, Integer> getTotalRetourQuantities() throws SQLException {
        logger.info("getTotalRetourQuantities");
        Map<Integer, Integer> retourQuantities = new HashMap<>();
        String sql = "SELECT id_article, SUM(quantity) AS total_quantity FROM retour GROUP BY id_article";

        try (Statement stmt = cnn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int articleId = rs.getInt("id_article");
                int totalQuantity = rs.getInt("total_quantity");
                retourQuantities.put(articleId, totalQuantity);
            }
        }
        return retourQuantities;
    }



}
