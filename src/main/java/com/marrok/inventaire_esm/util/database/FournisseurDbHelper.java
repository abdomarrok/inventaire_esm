package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDbHelper {

    public Connection cnn;

    public FournisseurDbHelper() throws SQLException {
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }
    public List<Fournisseur> getFournisseurs() {
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
                String email = resultSet.getString("EMAIL");  // Assuming EMAIL is stored as a number

                Fournisseur fournisseur = new Fournisseur(id, name, rc, nif, ai, nis, tel, fax, address, email);
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                e.printStackTrace();
                // Handle the exception according to your application's requirements
            }
        }

        return fournisseurs;
    }

    public int addFournisseur(Fournisseur fournisseur) {
        String query = "INSERT INTO fournisseur (name, rc, nif, ai, nis, tel, fax, address, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            return preparedStatement.executeUpdate(); // Return 1 if insertion succeeds

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
        }
    }
    public Fournisseur getFournisseurById(int idFournisseur) {
        String query = "SELECT id, name, rc, nif, ai, nis, tel, fax, address, email FROM fournisseur WHERE id = ?";
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
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return fournisseur;
    }

    // Method to update an existing fournisseur (supplier) in the database
    public int updateFournisseur(Fournisseur fournisseur) throws SQLException {
        // SQL query to update the fournisseur
        String query = "UPDATE fournisseur SET name = ?, rc = ?, nif = ?, ai = ?, nis = ?, tel = ?, fax = ?, address = ?, email = ? WHERE id = ?";

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
            preparedStatement.setInt(10, fournisseur.getId());

            // Execute the update and return the number of rows affected
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating fournisseur: " + e.getMessage());
        }
    }
    // Method to delete an existing fournisseur (supplier) from the database
    public boolean deleteFournisseur(int fournisseurId) {
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
            e.printStackTrace();
            return false; // Return false if an error occurred
        }
    }
}
