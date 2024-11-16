package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Fournisseur;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDbHelper {
    Logger logger =Logger.getLogger(FournisseurDbHelper.class);

    public Connection cnn;

    public FournisseurDbHelper() throws SQLException {
        logger.info("FournisseurDbHelper");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public List<Fournisseur> getFournisseurs() {
        logger.info("getFournisseurs");
        List<Fournisseur> fournisseurs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM fournisseur ORDER BY id DESC";
            statement = this.cnn.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String rc = resultSet.getString("RC");
                String nif = resultSet.getString("NIF");
                String ai = resultSet.getString("AI");
                String nis = resultSet.getString("NIS");
                String tel = resultSet.getString("TEL");
                String fax = resultSet.getString("FAX");
                String address = resultSet.getString("ADDRESS");
                String email = resultSet.getString("EMAIL");
                String rib=resultSet.getString("RIB");

                Fournisseur fournisseur = new Fournisseur(id, name, rc, nif, ai, nis, tel, fax, address, email,rib);
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
           logger.error(e);
            // Handle the exception according to your application's requirements
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
               logger.error(e);
            }
        }

        return fournisseurs;
    }

    public int addFournisseur(Fournisseur fournisseur) {
        logger.info("addFournisseur");
        String query = "INSERT INTO fournisseur (name, rc, nif, ai, nis, tel, fax, address, email,rib) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, fournisseur.getName());
            preparedStatement.setString(2, fournisseur.getRc());
            preparedStatement.setString(3, fournisseur.getNif());
            preparedStatement.setString(4, fournisseur.getAi());
            preparedStatement.setString(5, fournisseur.getNis());
            preparedStatement.setString(6, fournisseur.getTel());
            preparedStatement.setString(7, fournisseur.getFax());
            preparedStatement.setString(8, fournisseur.getAddress());
            preparedStatement.setString(9, fournisseur.getEmail());
            preparedStatement.setString(10, fournisseur.getRib());

            return preparedStatement.executeUpdate(); // Return 1 if insertion succeeds

        } catch (SQLException e) {
           logger.error(e);
            return -1; // Return -1 if an error occurs
        }
    }
    public Fournisseur getFournisseurById(int idFournisseur) {
        logger.info("getFournisseurById");
        String query = "SELECT id, name, rc, nif, ai, nis, tel, fax, address, email,rib FROM fournisseur WHERE id = ?";
        Fournisseur fournisseur = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, idFournisseur);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    fournisseur = new Fournisseur(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("rc"),
                            rs.getString("nif"),
                            rs.getString("ai"),
                            rs.getString("nis"),
                            rs.getString("tel"),
                            rs.getString("fax"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getString("rib")
                    );
                }
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return fournisseur;
    }

    // Method to update an existing fournisseur (supplier) in the database
    public int updateFournisseur(Fournisseur fournisseur) throws SQLException {
        logger.info("updateFournisseur");
        // SQL query to update the fournisseur
        String query = "UPDATE fournisseur SET name = ?, rc = ?, nif = ?, ai = ?, nis = ?, tel = ?, fax = ?, address = ?, email = ? ,rib =? WHERE id = ?";

        // Try-with-resources to ensure the resources are closed after execution
        try (
                PreparedStatement preparedStatement =  this.cnn.prepareStatement(query)) {

            // Set the parameters of the query from the fournisseur object
            preparedStatement.setString(1, fournisseur.getName());
            preparedStatement.setString(2, fournisseur.getRc());
            preparedStatement.setString(3, fournisseur.getNif());
            preparedStatement.setString(4, fournisseur.getAi());
            preparedStatement.setString(5, fournisseur.getNis());
            preparedStatement.setString(6, fournisseur.getTel());
            preparedStatement.setString(7, fournisseur.getFax());
            preparedStatement.setString(8, fournisseur.getAddress());
            preparedStatement.setString(9, fournisseur.getEmail());
            preparedStatement.setString(10, fournisseur.getRib());
            preparedStatement.setInt(11, fournisseur.getId());

            // Execute the update and return the number of rows affected
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
           logger.error(e);
            throw new SQLException("Error updating fournisseur: " + e.getMessage());
        }
    }
    // Method to delete an existing fournisseur (supplier) from the database
    public boolean deleteFournisseur(int fournisseurId) {
        logger.info("deleteFournisseur");
        // SQL query to delete the fournisseur
        String query = "DELETE FROM fournisseur WHERE id = ?";

        // Try-with-resources to ensure the resources are closed after execution
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            // Set the parameter of the query with the fournisseur ID
            preparedStatement.setInt(1, fournisseurId);

            // Execute the delete and check if a row was affected
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if the delete was successful
        } catch (SQLException e) {
            logger.error(e);
            return false; // Return false if an error occurred
        }
    }
}
