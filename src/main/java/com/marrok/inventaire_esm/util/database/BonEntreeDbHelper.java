package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonEntreeDbHelper {
    Logger logger = LogManager.getLogger(BonEntreeDbHelper.class);
    public Connection cnn;

    public BonEntreeDbHelper() throws SQLException {
        logger.info("BonEntreeDbHelper started");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public int createBonEntree(BonEntree bonEntree) {
        logger.info("createBonEntree called");
        String query = "INSERT INTO bon_entree (id_fournisseur, date, TVA,document_num) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, bonEntree.getIdFournisseur());
            preparedStatement.setDate(2, new java.sql.Date(bonEntree.getDate().getTime()));
            preparedStatement.setInt(3, bonEntree.getTva());
            preparedStatement.setString(4, bonEntree.getDocumentNum());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated ID
                    }
                }
            }
        } catch (SQLException e) {
          logger.error(e);
        }
        return -1; // Return -1 if failed to create Bon Entree
    }
    public List<BonEntree> getBonEntrees() {
        logger.info("getBonEntrees");
        List<BonEntree> bonEntrees = new ArrayList<>();
        String query = "SELECT id, id_fournisseur, date, TVA, document_num FROM bon_entree Order by last_edited desc;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BonEntree bonEntree = new BonEntree(
                        rs.getInt("id"),
                        rs.getInt("id_fournisseur"),
                        rs.getDate("date"),
                        rs.getInt("TVA"),
                        rs.getString("document_num")
                );
                bonEntrees.add(bonEntree);
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return bonEntrees;
    }
    public List<Entree> getEntreesById_BonEntreeId(int bonEntreeId) {
        logger.info("getEntreesById_BonEntreeId called");
        List<Entree> entrees = new ArrayList<>();
        String query = "SELECT * FROM entree WHERE id_be = ?"; // Filtering by the BonEntree ID

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, bonEntreeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idArticle = resultSet.getInt("id_article");
                int quantity = resultSet.getInt("quantity");
                double unitPrice = resultSet.getDouble("unit_price");
                // Create a new Entree object based on your class structure
                Entree entree = new Entree(id, idArticle, quantity, unitPrice, bonEntreeId); // Adjust the constructor parameters accordingly
                entrees.add(entree);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return entrees; // Return the list of Entree objects
    }
    public BonEntree getBonEntreesById(int idBonEntree) {
        logger.info("getBonEntreesById called");
        String query = "SELECT id, id_fournisseur, date, TVA, document_num FROM bon_entree WHERE id = ?";
        BonEntree bonEntree = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, idBonEntree);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    bonEntree = new BonEntree(
                            rs.getInt("id"),
                            rs.getInt("id_fournisseur"),
                            rs.getTimestamp("date"), // Use getTimestamp for datetime
                            rs.getInt("TVA"),
                            rs.getString("document_num")
                    );
                }
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return bonEntree;
    }

    public boolean saveEntree(Entree entree) {
        logger.info("saveEntree called");
        String query = "INSERT INTO entree (id_article, quantity, unit_price, id_be) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, entree.getIdArticle());
            preparedStatement.setInt(2, entree.getQuantity());
            preparedStatement.setDouble(3, entree.getUnitPrice());
            preparedStatement.setInt(4, entree.getIdBe()); // Set Bon Entree ID

            return preparedStatement.executeUpdate() > 0; // Return true if insertion succeeds
        } catch (SQLException e) {
          logger.error(e);
            return false; // Return false if an error occurs
        }
    }
}
