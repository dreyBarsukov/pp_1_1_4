package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection = Util.getConnection();

    @Override
    public void createUsersTable() {
        String sqlCreate = """
                CREATE TABLE IF NOT EXISTS USER (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       name VARCHAR(128),
                                       last_name VARCHAR(128),
                                       age TINYINT
                                      );
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCreate);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        String sqlDrop = """
                DROP TABLE IF EXISTS USER;
                                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDrop);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sqlSave = """
                INSERT INTO simple.USER (name, last_name, age) 
                VALUES (?,?,?);
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSave);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
            System.out.printf("User с именем %s добавлен в базу данных\n", name);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sqlRemove = """
                DELETE FROM simple.USER
                WHERE id = ?;
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRemove);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGet = """
                SELECT *
                FROM simple.USER;
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                ));
            }
            System.out.println(result);
            preparedStatement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        String sqlClean = """
                DELETE FROM USER;
                                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlClean);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
