package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.StockAdjustment;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleDbHelper {
     public   ArticleDbHelper()  throws SQLException {
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public Connection cnn;
    public List<String> getAllArticlesNames() {
        List<String> articles = new ArrayList<>();
        String query = "SELECT name FROM article ORDER BY name DESC;";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                articles.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting  articles ", e);
        }
        return articles;
    }

    // Method to fetch all articles
    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT id, name, unite, remarque, description, id_category, last_edited FROM article ORDER BY last_edited DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unite"),
//                        rs.getInt("quantity"),
                        rs.getString("remarque"),
                        rs.getString("description"),
                        rs.getInt("id_category")
                );
                articles.add(article);
            }
        } catch (SQLException e) {

            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting  articles ", e);

            // Handle exception
        }
        return articles;
    }

    public Article getArticleById(long id) {
        String query = "SELECT * FROM article WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Article(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("unite"),
//                            rs.getInt("quantity"),
                            rs.getString("remarque"),
                            rs.getString("description"),
                            rs.getInt("id_category")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting  article with id "+id, e);
            // Handle exception
        }
        return null; // Return null if article is not found
    }

    public boolean addArticle(Article article) {
        String query = "INSERT INTO article (name, unite,  remarque, description, id_category) VALUES (?, ?, ?,  ?, ?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setString(3, article.getRemarque());
            stmt.setString(4, article.getDescription());
            stmt.setInt(5, article.getIdCategory());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public void addArticles(List<Article> articles) throws SQLException {
        for (Article article : articles) {
            addArticle(article);
        }
    }



    public boolean updateArticle(Article article) {
        String query = "UPDATE article SET name = ?, unite = ?, remarque = ?, description = ?, id_category = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setString(3, article.getRemarque());
            stmt.setString(4, article.getDescription());
            stmt.setInt(5, article.getIdCategory());
            stmt.setLong(6, article.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error Adding article "+article.getName(), e);

        }
        return false;
    }

    public boolean deleteArticle(long id) {
        String query = "DELETE FROM article WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {

            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لا تستطيع حذف هذا العنصر لانه مازال مرفقا بجرد او وصل استلام");
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error deleting  article with id "+id, e);
        }
        return false;
    }
    // Method to fetch article ID by name
    public  int getArticleIdByName(String name) {
        String query = "SELECT id FROM article WHERE name = ?";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting article id by its name: "+name, e);
        }
        return -1; // Return -1 if not found
    }
    public int getTotalQuantityByArticleId(int articleId) {
        int totalEntree = getTotalEntredQuantityByArticleId(articleId);
        int totalSortie = getTotalSortieQuantityByArticleId(articleId);
        int totalAdjustment = getTotalAdjustmentByArticleId(articleId);

        return totalEntree - totalSortie + totalAdjustment;  // Net quantity
    }

    public int getTotalAdjustmentByArticleId(int articleId) {
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
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE,
                    "Error getting Total Adjustment Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no adjustments are found
    }

    public int getTotalEntredQuantityByArticleId(int articleId) {
        String query = "SELECT COALESCE(SUM(quantity), 0) AS total_entree FROM entree WHERE id_article = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_entree");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE,
                    "Error getting Total Entrée Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no entries are found
    }
    public int getTotalSortieQuantityByArticleId(int articleId) {
        String query = "SELECT COALESCE(SUM(quantity), 0) AS total_sortie FROM sortie WHERE id_article = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_sortie");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE,
                    "Error getting Total Sortie Quantity By ArticleId: " + articleId, e);
        }

        return 0;  // Return 0 if an error occurs or no records are found
    }

    public Map<Integer, Integer> getTotalQuantitiesByArticle() {
        Map<Integer, Integer> totalQuantities = new HashMap<>();

        String query = "SELECT article.id AS article_id, " +
                "(SELECT COALESCE(SUM(entree.quantity), 0) FROM entree WHERE entree.id_article = article.id) AS total_entree, " +
                "(SELECT COALESCE(SUM(sortie.quantity), 0) FROM sortie WHERE sortie.id_article = article.id) AS total_sortie, " +
                "(SELECT COALESCE(SUM(CASE WHEN adjustment_type = 'increase' THEN quantity ELSE 0 END), 0) - " +
                "COALESCE(SUM(CASE WHEN adjustment_type = 'decrease' THEN quantity ELSE 0 END), 0) " +
                "FROM stock_adjustment WHERE article_id = article.id) AS net_adjustment " +
                "FROM article " +
                "ORDER BY (total_entree - total_sortie + net_adjustment) DESC, article.id ASC"; // Sort by total quantity, then article id

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("article_id");  // Use alias 'article_id'
                int totalQuantity = resultSet.getInt("total_entree") - resultSet.getInt("total_sortie") + resultSet.getInt("net_adjustment");
                totalQuantities.put(articleId, totalQuantity);
            }
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting Total Quantity By Article", e);
        }

        return totalQuantities;
    }



    // READ: Get a single stock adjustment by ID
    public StockAdjustment getStockAdjustmentById(int id) {
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
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting stock adjustment by ID", e);
        }
        return null;
    }

    // READ: Get all stock adjustments
    public List<StockAdjustment> getAllStockAdjustments() {
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
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error getting stock adjustments", e);
        }
        return adjustments;
    }


    // DELETE: Remove a stock adjustment by ID
    public boolean deleteStockAdjustment(int id) {
        String query = "DELETE FROM stock_adjustment WHERE id = ?";
        try (PreparedStatement stmt = cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error deleting stock adjustment", e);
            return false;
        }
    }

    // CREATE: Add a new stock adjustment
    public boolean addStockAdjustment(StockAdjustment adjustment) {
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
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error adding stock adjustment", e);
            return false;
        }
    }

    // UPDATE: Modify an existing stock adjustment
    public boolean updateStockAdjustment(StockAdjustment adjustment) {
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
            Logger.getLogger(ArticleDbHelper.class.getName()).log(Level.SEVERE, "Error updating stock adjustment", e);
            return false;
        }
    }



}
