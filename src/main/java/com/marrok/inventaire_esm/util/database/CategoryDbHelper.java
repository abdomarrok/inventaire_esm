package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDbHelper {
    Logger logger = LogManager.getLogger(CategoryDbHelper.class);
    public Connection cnn;
   public CategoryDbHelper()  throws SQLException {
       logger.info("CategoryDbHelper started");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public List<Category> getCategories() {
       logger.info("getCategories");
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, name_cat FROM category ORDER BY id DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("id"), rs.getString("name_cat"));
                categories.add(category);
            }
        } catch (SQLException e) {
         logger.error(e);
        }
        return categories;
    }

    public boolean addCategory(Category category) {
       logger.info("addCategory");
        String query = "INSERT INTO category (name_cat) VALUES (?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error(e);
        }
        return false;
    }

    public boolean updateCategory(Category category) {
       logger.info("updateCategory");
        String query = "UPDATE category SET name_cat = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error(e);
            // Handle exception
        }
        return false;
    }

    public boolean deleteCategory(long id) {
       logger.info("deleteCategory");
        String query = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل حذف الفئة", e.getMessage());
        }
        return false;
    }

    public int getCategoryByName(String categoryName) {
       logger.info("getCategoryByName");
        String query = "SELECT id FROM category WHERE name_cat = ? ORDER BY name_cat DESC;";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
          logger.error(e);
        }
        return -1; // Return -1 if category name is not found or in case of an error
    }

    public String getCategoryById(int categoryId) {
       logger.info("getCategoryById");
        String query = "SELECT name_cat FROM category WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name_cat");
                }
            }
        } catch (SQLException e) {
          logger.error(e);
        }
        return "";
    }

}
