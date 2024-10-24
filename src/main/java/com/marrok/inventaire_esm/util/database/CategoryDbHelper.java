package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDbHelper {
    public Connection cnn;
   public CategoryDbHelper()  throws SQLException {
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, name_cat FROM category ORDER BY id DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("id"), rs.getString("name_cat"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return categories;
    }

    public boolean addCategory(Category category) {
        String query = "INSERT INTO category (name_cat) VALUES (?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean updateCategory(Category category) {
        String query = "UPDATE category SET name_cat = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean deleteCategory(long id) {
        String query = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل حذف الفئة", e.getMessage());
        }
        return false;
    }

    public int getCategoryByName(String categoryName) {
        String query = "SELECT id FROM category WHERE name_cat = ? ORDER BY name_cat DESC;";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return -1; // Return -1 if category name is not found or in case of an error
    }

    public String getCategoryById(int categoryId) {
        String query = "SELECT name_cat FROM category WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name_cat");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return ""; // Return an empty string if category name is not found or an error occurs
    }

}
