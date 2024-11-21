package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.BonRetour;
import com.marrok.inventaire_esm.model.Retour;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonRetourDbHelper {
    Logger logger = Logger.getLogger(BonRetourDbHelper.class);
    public Connection cnn;

    public BonRetourDbHelper() throws SQLException {
        logger.debug("BonRetourDbHelper");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public int createBonRetour(BonRetour bonRetour) {
        logger.info("Create BonRetour");
        String query = "INSERT INTO bon_retour (id_employeur, id_service, date, return_reason, last_edited) " +
                "VALUES (?, ?, ?, ?, DEFAULT)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, bonRetour.getIdEmployeur());
            preparedStatement.setInt(2, bonRetour.getIdService());
            preparedStatement.setDate(3, new java.sql.Date(bonRetour.getDate().getTime()));
            preparedStatement.setString(4, bonRetour.getReturnReason());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated BonRetour ID
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return -1; // Return -1 if creation failed
    }

    public boolean saveRetour(Retour retour) {
        logger.info("Save Retour");
        String query = "INSERT INTO retour (id_article, quantity, id_br) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, retour.getIdArticle());
            preparedStatement.setInt(2, retour.getQuantity());
            preparedStatement.setInt(3, retour.getIdBr()); // Set BonRetour ID

            return preparedStatement.executeUpdate() > 0; // Return true if insertion succeeds
        } catch (SQLException e) {
            logger.error(e);
            return false; // Return false if an error occurs
        }
    }

    public List<Retour> getRetoursByIdBonRetour(int idBonRetour) {
        logger.info("Get Retours by idBonRetour");
        List<Retour> retours = new ArrayList<>();
        String query = "SELECT * FROM retour WHERE id_br = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, idBonRetour);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idArticle = resultSet.getInt("id_article");
                int quantity = resultSet.getInt("quantity");

                Retour retour = new Retour(id, idArticle, quantity, idBonRetour);
                retours.add(retour);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return retours;
    }

    public BonRetour getBonRetourById(int id) {
        logger.info("Get BonRetour by id");
        String query = "SELECT id, id_employeur, id_service, date, return_reason, last_edited FROM bon_retour WHERE id = ?";
        BonRetour bonRetour = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    bonRetour = new BonRetour(
                            rs.getInt("id"),
                            rs.getInt("id_employeur"),
                            rs.getInt("id_service"),
                            rs.getDate("date"),
                            rs.getString("return_reason"),
                            rs.getTimestamp("last_edited")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return bonRetour;
    }

    public List<BonRetour> getBonRetours() {
        logger.info("Get BonRetours");
        List<BonRetour> bonRetours = new ArrayList<>();
        String query = "SELECT id, id_employeur, id_service, date, return_reason, last_edited FROM bon_retour ORDER BY last_edited DESC";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BonRetour bonRetour = new BonRetour(
                        rs.getInt("id"),
                        rs.getInt("id_employeur"),
                        rs.getInt("id_service"),
                        rs.getDate("date"),
                        rs.getString("return_reason"),
                        rs.getTimestamp("last_edited")
                );
                bonRetours.add(bonRetour);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return bonRetours;
    }
}
