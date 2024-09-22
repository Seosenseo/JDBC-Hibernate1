package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final UserDaoJDBCImpl INSTANCE = new UserDaoJDBCImpl();
    public static UserDaoJDBCImpl getInstance(){
        return INSTANCE;
    }
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE users ( 
                id SERIAL PRIMARY KEY , 
                name TEXT NOT NULL , 
                lastname TEXT NOT NULL , 
                age SMALLINT NOT NULL )
                """;

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
            System.out.println("Table [users] created");
        } catch (SQLException e) {
            System.out.println("Fail to create [users] TABLE");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String sql = """
                DROP TABLE users
                """;

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
            System.out.println("Table [users] dropped");
        } catch (SQLException | RuntimeException | ClassNotFoundException e) {
            System.out.println("Exception. Table may not exist");
//            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String saveSQL = "INSERT INTO users(name, lastName, age)" +
                         "VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveSQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
            System.out.println("User: " + name + " " + lastName + " " + age +" added.");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("User " + id + "(id) has been removed");
        } catch (SQLException e) {
            System.out.println("Exception.User cant be remove");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    public List<User> getAllUsers() {
        String sql = """
                SELECT id,
                name,
                lastname,
                age
                FROM users
                """;
        List<User> listUsers = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listUsers.add(getUserFromResultSet(resultSet));
            }
            System.out.println("All users got from [users] table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return listUsers;
    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE TABLE users
                """;

        try (Connection connection = Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } ;
    }



    private User getUserFromResultSet(ResultSet resultSet) {
        User user = new User();

        try {
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getByte("age"));
        } catch (SQLException e) {

            System.out.println("Failed ::getUserFromResultSet");
        }
        return user;
    }
}
