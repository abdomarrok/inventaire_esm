package com.marrok.inventaire_esm.util;

import com.marrok.inventaire_esm.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {


    public Connection cnn;

public DatabaseHelper() throws SQLException {
    this.cnn = DatabaseConnection.getInstance().getConnection();
}

    public String getUserNameById(int userId) {
        String query = "SELECT username FROM user WHERE id = ?";
        String username = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return username;
    }

    public String getUserRoleById(int userId) {
        String query = "SELECT role FROM user WHERE id = ?";
        String role = null;

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception, log error or throw a custom exception
        }

        return role;
    }

    public boolean addUser(User user) {
        String query = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)){

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET username = ?, password = ?, role = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to update user in the database", e);
        }
    }

    public int getUserIdByName(String username) {
        String query = "SELECT id FROM user WHERE username = ?";
        int userId = -1; // Return -1 if the user is not found

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return userId;
    }


    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, role FROM user";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                User user = new User(id, username, password, role);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to fetch users from the database", e);
        }

        return users;
    }

    // Example method for deleting a user
    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to delete user from the database", e);
        }
    }


    // Method to fetch all article names
    public List<String> getAllArticlesNames() {
        List<String> articles = new ArrayList<>();
        String query = "SELECT name FROM article";
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
        String query = "SELECT id, name, unite, quantity, remarque, description, id_category FROM article";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unite"),
                        rs.getInt("quantity"),
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


    public boolean validateLogin(String username, String password) {
        String query = "SELECT id FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    SessionManager.setActiveUserId(userId); // Set the active user ID
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
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
                            rs.getInt("quantity"),
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
        String query = "INSERT INTO article (name, unite, quantity, remarque, description, id_category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setInt(3, article.getQuantity());
            stmt.setString(4, article.getRemarque());
            stmt.setString(5, article.getDescription());
            stmt.setInt(6, article.getIdCategory());
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
        String query = "UPDATE article SET name = ?, unite = ?, quantity = ?, remarque = ?, description = ?, id_category = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, article.getName());
            stmt.setString(2, article.getUnite());
            stmt.setInt(3, article.getQuantity());
            stmt.setString(4, article.getRemarque());
            stmt.setString(5, article.getDescription());
            stmt.setInt(6, article.getIdCategory());
            stmt.setLong(7, article.getId());
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

            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لا تستطيع حذف هذا العنصر لانه مازال مرفقا بجرد");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT id, name_cat FROM category";

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
            // Handle exception
        }
        return false;
    }

    public int getCategoryByName(String categoryName) {
        String query = "SELECT id FROM category WHERE name_cat = ?";
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

    public List<Service> getServices() {
        List<Service> services = new ArrayList<>();
        String query = "SELECT id, name FROM service";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Service service = new Service(rs.getInt("id"), rs.getString("name"));
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return services;
    }

    public boolean addService(Service service) {
        String query = "INSERT INTO service (name) VALUES (?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean updateService(Service service) {
        String query = "UPDATE service SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean deleteService(long id) {
        String query = "DELETE FROM service WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }
    public List<Localisation> getLocalisations() {
        List<Localisation> localisations = new ArrayList<>();
        String query = "SELECT * FROM localisation";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Localisation localisation = new Localisation(
                        rs.getInt("id"),
                        rs.getString("loc_name"),
                        rs.getInt("floor"),
                        rs.getInt("id_service")
                );
                localisations.add(localisation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return localisations;
    }

    public boolean addLocalisation(Localisation localisation) {
        String query = "INSERT INTO localisation ( `loc_name`, `floor`, `id_service`) VALUES ( ?,?,?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, localisation.getLocName());
            stmt.setInt(2, localisation.getFloor());
            stmt.setInt(3, localisation.getIdService());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean updateLocalisation(Localisation localisation) {
        String query = "UPDATE localisation SET `loc_name` = ?, `floor` = ?, `id_service` = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, localisation.getLocName());
            stmt.setInt(2, localisation.getFloor());
            stmt.setInt(3, localisation.getIdService());
            stmt.setInt(4, localisation.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean deleteLocalisation(long id) {
        String query = "DELETE FROM localisation WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }  catch (SQLIntegrityConstraintViolationException e) {

            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لا تستطيع حذف هذا المكان لانه مازال مرفقا بجرد");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Localisation> getLocalisationsByServiceId(int serviceId) {
        List<Localisation> localisations = new ArrayList<>();
        String query = "SELECT * FROM Localisation WHERE id_service = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Localisation localisation = new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                    localisations.add(localisation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return localisations;
    }

    public Localisation getLocalisationByName(String name) {
        String query = "SELECT * FROM Localisation WHERE loc_name = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if localisation is not found
    }

    public Localisation getLocalisationById(int id) {
        String query = "SELECT * FROM localisation WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if localisation is not found
    }


    public Service getServiceById(int id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Service service = null;

        try {
            String query = "SELECT * FROM service WHERE id = ?";
            stmt = this.cnn.prepareStatement(query); // Using this.cnn to get the connection
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int serviceId = rs.getInt("id");
                String serviceName = rs.getString("name");
                service = new Service(serviceId, serviceName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
        return service;
    }
    public Service getServiceByName(String serviceName) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Service service = null;

        try {
            String query = "SELECT * FROM service WHERE name = ?";
            stmt = this.cnn.prepareStatement(query); // Using this.cnn to get the connection
            stmt.setString(1, serviceName);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int serviceId = rs.getInt("id");
                String name = rs.getString("name");
                service = new Service(serviceId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
        return service;
    }




    public List<Inventaire_Item> getInventaireItems() {
        List<Inventaire_Item> inventaireItems = new ArrayList<>();

        String query = "SELECT * FROM inventaire_item ORDER BY time DESC";

        try (
             PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {
                int id_article = resultSet.getInt("id_article");
                int id_user = resultSet.getInt("user_id");
                int id_inventaire = resultSet.getInt("id");
                int id_localisation = resultSet.getInt("id_localisation");
                int employer_id = resultSet.getInt("id_employer");

                Time time = resultSet.getTime("time");
                String timeString = time != null ? time.toString() : ""; // Handle null value gracefully

                // Combine the current date with the time from the database
                LocalDate currentDate = LocalDate.now();
                LocalTime dbTime = time.toLocalTime();
                LocalDateTime dateTime = LocalDateTime.of(currentDate, dbTime);

                // Format the LocalDateTime to the desired string format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String formattedDateTime = dateTime.format(formatter);

                String num_inventaire = resultSet.getString("num_inventaire");
                String status=resultSet.getString("status");

                Inventaire_Item item = new Inventaire_Item(id_inventaire, id_article, id_localisation, id_user,employer_id,  num_inventaire,formattedDateTime,status);
                inventaireItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        return inventaireItems;
    }
    public void insertInventaireItems(List<Inventaire_Item> inventaireItems) throws SQLException {
        String query = "INSERT INTO inventaire_item (id_article, user_id, id_localisation, id_employer, num_inventaire, time,status) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            for (Inventaire_Item item : inventaireItems) {
                preparedStatement.setInt(1, item.getArticle_id());
                preparedStatement.setInt(2, item.getUser_id());
                preparedStatement.setInt(3, item.getLocalisation_id());
                preparedStatement.setInt(4, item.getEmployer_id());
                preparedStatement.setString(5, item.getNum_inventaire());

                // Parse the formatted date time string back to a SQL Timestamp
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(item.getFormattedDateTime(), formatter);
                preparedStatement.setTimestamp(6, Timestamp.valueOf(dateTime));
                preparedStatement.setString(7, item.getStatus());

                preparedStatement.addBatch();
            }

            // Execute the batch
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Re-throw the exception for higher-level handling
        }
    }


    public  int addEmployer(String firstName, String lastName, String title) {
        String query = "INSERT INTO employeur (firstname, lastname, title) VALUES (?, ?, ?)";

        try (
             PreparedStatement preparedStatement = this.cnn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, title);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                // Retrieve the generated keys to get the ID of the newly inserted employer
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return the ID of the newly inserted employer
                }
            }

            return -1; // Return -1 if the insertion failed or if the ID couldn't be retrieved
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 if an exception occurred
        }
    }
    public boolean updateEmployer(Employer employer) {
        String query = "UPDATE employeur SET firstname = ?, lastname = ?, title = ? WHERE id = ?";
        try (PreparedStatement pstmt = this.cnn.prepareStatement(query)) {
            pstmt.setString(1, employer.getFirstName());
            pstmt.setString(2, employer.getLastName());
            pstmt.setString(3, employer.getTitle());
            pstmt.setInt(4, employer.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ObservableList<Employer> getEmployers() {
        ObservableList<Employer> employerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM employeur";
        try (Statement stmt = this.cnn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employer employer = new Employer(rs.getInt("id"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("title"));
                employerList.add(employer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employerList;
    }
    public boolean deleteEmployer(int employerId) {
        String query = "DELETE FROM employeur WHERE id = ?";
        try (
                PreparedStatement pstmt = this.cnn.prepareStatement(query)) {
            pstmt.setInt(1, employerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public  String getEmployerFullNameById(int employerId) {
        String query = "SELECT firstname, lastname FROM employeur WHERE id = ?";

        try (
             PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
             preparedStatement.setInt(1, employerId);
             ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                return firstName + " " + lastName;
            } else {
                return null; // Return null if no employer found with the given ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Return null if an exception occurred
        }
    }

    // Method to fetch all employer names
    public  List<String> getAllEmployersNames() {
        List<String> employers = new ArrayList<>();
        String query = "SELECT CONCAT(firstname, ' ', lastname) AS fullname FROM employeur";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                employers.add(rs.getString("fullname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employers;
    }

    public  List<Employer> getAllEmployers() {
        List<Employer> employers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            String query = "SELECT * FROM employeur";
            statement = this.cnn.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String title = resultSet.getString("title");

                Employer employer = new Employer(id, firstName, lastName, title);
                employers.add(employer);
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

        return employers;
    }

    // Method to fetch all location names
    public  List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String query = "SELECT loc_name FROM localisation";
        try (
             PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("loc_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    // Method to fetch employer ID by name
    public  int getEmployerIdByName(String fullname) {
        String query = "SELECT id FROM employeur WHERE CONCAT(firstname, ' ', lastname) = ?";
        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, fullname);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
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

    // Method to fetch location ID by name
    public  int getLocationIdByName(String name) {
        String query = "SELECT id FROM localisation WHERE loc_name = ?";
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

    public  boolean deleteInventaireItem(int itemId) {
        String query = "DELETE FROM inventaire_item WHERE id = ?";
        try (
             PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public  boolean updateInventaireItem(Inventaire_Item inventaireItem) {
        System.out.println("from dbhelper"+inventaireItem.toString());
        String query = "UPDATE inventaire_item SET id_article = ?, id_employer = ? , id_localisation = ?, user_id = ?, time = ?, num_inventaire = ? ,status= ? WHERE id = ?";
        try (
             PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, inventaireItem.getArticle_id());
            stmt.setInt(2, inventaireItem.getEmployer_id());
            stmt.setInt(3, inventaireItem.getLocalisation_id());
            stmt.setInt(4, inventaireItem.getUser_id());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(6, inventaireItem.getNum_inventaire());
            stmt.setString(7, inventaireItem.getStatus());
            stmt.setInt(8, inventaireItem.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addInventaireItem(Inventaire_Item item) {
        String query = "INSERT INTO inventaire_item (id_localisation, user_id, id_article, time, id_employer, num_inventaire, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setInt(1, item.getLocalisation_id());
            preparedStatement.setInt(2, item.getUser_id());
            preparedStatement.setInt(3, item.getArticle_id());

            // Use DateTimeFormatter to parse the date in "2024-09-26" format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(item.getFormattedDateTime(), formatter);

            // Convert LocalDate to LocalDateTime at the start of the day (00:00:00)
            LocalDateTime localDateTime = localDate.atStartOfDay();

            // Set the parsed LocalDateTime as a Timestamp
            preparedStatement.setTimestamp(4, Timestamp.valueOf(localDateTime));
            preparedStatement.setInt(5, item.getEmployer_id());
            preparedStatement.setString(6, item.getNum_inventaire());
            preparedStatement.setString(7, item.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
    }




    public Inventaire_Item getInevntaireItemById(int id) {
        String query = "SELECT * FROM inventaire_item WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Convert Timestamp to LocalDateTime
                    LocalDateTime dateTime = rs.getTimestamp("time").toLocalDateTime();

                    // Format the LocalDateTime to "dd/MM/yyyy"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = dateTime.format(formatter);

                    return new Inventaire_Item(
                            rs.getInt("id"),
                            rs.getInt("id_article"),
                            rs.getInt("id_localisation"),
                            rs.getInt("user_id"),
                            rs.getInt("id_employer"),
                            rs.getString("num_inventaire"),
                            formattedDate,  // Store the formatted date as a string
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if inventaire_item is not found
    }




    public List<Inventaire_Item> getInventoryItemsByFilters(Integer serviceId, Integer localisationId, Integer year) throws SQLException {
        List<Inventaire_Item> inventoryItems = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT ii.*, l.id_service " +
                "FROM inventaire_item ii " +
                "JOIN localisation l ON ii.id_localisation = l.id " +
                "WHERE YEAR(ii.time) = ?");

        // Add service and localisation filters only if they are provided (non-null)
        if (serviceId != null) {
            query.append(" AND l.id_service = ?");
        }
        if (localisationId != null) {
            query.append(" AND ii.id_localisation = ?");
        }

        try (PreparedStatement statement = this.cnn.prepareStatement(query.toString())) {
            // Set the year parameter
            statement.setInt(1, year);

            int parameterIndex = 2; // Start at 2 because year is the first parameter

            // Set service and localisation parameters if they are not null
            if (serviceId != null) {
                statement.setInt(parameterIndex++, serviceId);
            }
            if (localisationId != null) {
                statement.setInt(parameterIndex++, localisationId);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int articleId = resultSet.getInt("id_article");
                int localisationIdDb = resultSet.getInt("id_localisation");
                int userId = resultSet.getInt("user_id");
                int employerId = resultSet.getInt("id_employer");
                String numInventaire = resultSet.getString("num_inventaire");
                String status = resultSet.getString("status");

                // Get the date as a string
                String formattedDateTime = resultSet.getTimestamp("time").toString();

                // Create an instance of Inventaire_Item using the appropriate constructor
                Inventaire_Item item = new Inventaire_Item(id, articleId, localisationIdDb, userId, employerId, numInventaire, formattedDateTime,status);

                inventoryItems.add(item);
            }
        }

        return inventoryItems;
    }



}
