package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.BonSortie;
import com.marrok.inventaire_esm.model.Sortie;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonSortieDbHelper {
    Logger logger = Logger.getLogger(BonSortieDbHelper.class);
    public Connection cnn;

    public BonSortieDbHelper() throws SQLException {
        logger.debug("BonSortieDbHelper");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public int createBonSortie(BonSortie bonSortie) {
        logger.info("Create BonSortie");
        String query = "INSERT INTO bon_sortie (id_employeur, id_service, date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, bonSortie.getIdEmployeur());
            preparedStatement.setInt(2, bonSortie.getIdService());
            preparedStatement.setDate(3, new java.sql.Date(bonSortie.getDate().getTime()));  // Use the java.sql.Date for LocalDate

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated BonSortie ID
                    }
                }
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return -1; // Return -1 if failed to create Bon Sortie
    }
    public boolean saveSortie(Sortie sortie) {
        logger.info("Save Sortie");
        String query = "INSERT INTO sortie (id_article, quantity, id_bs) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, sortie.getIdArticle());
            preparedStatement.setInt(2, sortie.getQuantity());
            preparedStatement.setInt(3, sortie.getIdBs());  // Set Bon Sortie ID

            return preparedStatement.executeUpdate() > 0;  // Return true if insertion succeeds
        } catch (SQLException e) {
           logger.error(e);
            return false; // Return false if an error occurs
        }
    }
    public List<Sortie> getSortiesByIdBonSortieId(int idBonSortie) {
        logger.info("Get Sorties by idBonSortie");
        List<Sortie> sorties = new ArrayList<>();
        String query = "SELECT * FROM sortie WHERE id_bs = ?"; // Filtering by the BonSortie ID

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, idBonSortie); // Bind the BonSortie ID to the query
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idArticle = resultSet.getInt("id_article");
                int quantity = resultSet.getInt("quantity");

                Sortie sortie = new Sortie(id, idArticle, quantity,  idBonSortie); // Adjust constructor if needed
                sorties.add(sortie);
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return sorties; // Return the list of Sortie objects
    }
    public BonSortie getBonSortiesById(int id) {
        logger.info("Get BonSorties by id");
        String query = "SELECT id, id_employeur, id_service, date, last_edited FROM bon_sortie WHERE id = ?";
        BonSortie bonSortie = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    bonSortie = new BonSortie(
                            rs.getInt("id"),
                            rs.getInt("id_employeur"),
                            rs.getInt("id_service"),
                            rs.getTimestamp("date")
                    );
                }
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return bonSortie;
    }

    public List<BonSortie> getBonSorties() {
        logger.info("Get BonSorties");
        List<BonSortie> bonSorties = new ArrayList<>();
        String query = "SELECT id, id_employeur, id_service, date, last_edited FROM bon_sortie ORDER BY last_edited DESC";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BonSortie bonSortie = new BonSortie(
                        rs.getInt("id"),  // id of the bon_sortie
                        rs.getInt("id_employeur"),  // id of the employeur
                        rs.getInt("id_service"),  // id of the service
                        rs.getDate("date")  // Converting SQL timestamp to LocalDateTime

                );
                bonSorties.add(bonSortie);
            }
        } catch (SQLException e) {
           logger.error(e);
        }

        return bonSorties;
    }
}
