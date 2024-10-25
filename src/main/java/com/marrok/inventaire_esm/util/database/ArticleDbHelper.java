package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if article is not found
    }

    public boolean addArticle(Article article) {
        String query = "INSERT INTO article (name, unite,  remarque, description, id_category) VALUES (?, ?, ?,  ?, ?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
//            stmt.setInt(3, article.getQuantity());
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
//            stmt.setInt(3, article.getQuantity());
            stmt.setString(3, article.getRemarque());
            stmt.setString(4, article.getDescription());
            stmt.setInt(5, article.getIdCategory());
            stmt.setLong(6, article.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
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
            throw new RuntimeException(e);
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
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }
    public int getTotalQuantityByArticleId(int articleId) {
        String query = "SELECT " +
                "(SELECT COALESCE(SUM(entree.quantity), 0) FROM entree WHERE entree.id_article = ?) AS total_entree, " +
                "(SELECT COALESCE(SUM(sortie.quantity), 0) FROM sortie WHERE sortie.id_article = ?) AS total_sortie";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, articleId);
            preparedStatement.setInt(2, articleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int totalEntree = resultSet.getInt("total_entree");
                    int totalSortie = resultSet.getInt("total_sortie");
                    return totalEntree - totalSortie;  // Return the net stock for the article
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  // Return -1 if an error occurs
    }


    public Map<Integer, Integer> getTotalQuantitiesByArticle() {
        Map<Integer, Integer> totalQuantities = new HashMap<>();

        // Updated SQL query using subqueries to avoid duplication
        String query = "SELECT article.id AS article_id, " +
                "(SELECT COALESCE(SUM(entree.quantity), 0) FROM entree WHERE entree.id_article = article.id) AS total_entree, " +
                "(SELECT COALESCE(SUM(sortie.quantity), 0) FROM sortie WHERE sortie.id_article = article.id) AS total_sortie " +
                "FROM article " +
                "ORDER BY total_entree - total_sortie DESC, article.id ASC"; // Sort by total quantity, then article id

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int articleId = resultSet.getInt("article_id");  // Use alias 'article_id'
                int totalQuantity = resultSet.getInt("total_entree") - resultSet.getInt("total_sortie");
                totalQuantities.put(articleId, totalQuantity);
            }
        } catch (SQLException e) {
            // Use a logger instead of printStackTrace in production
            e.printStackTrace();
        }

        return totalQuantities;
    }

}
