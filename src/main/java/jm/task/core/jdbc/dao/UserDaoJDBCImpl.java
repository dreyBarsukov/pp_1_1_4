package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String SQL = """
                CREATE TABLE USER (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       name VARCHAR(128),
                                       lastName VARCHAR(128),
                                       age INT
                                      );
                """;
        try (Connection connection = Util.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.executeUpdate();
        } catch (SQLSyntaxErrorException e) {
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String SQL = """
                DROP TABLE USER;
                                """;
        try (Connection connection = Util.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.executeUpdate();
        } catch (SQLSyntaxErrorException e) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String SQL = """
                INSERT INTO simple.USER (name, lastName, age) 
                VALUES (?,?,?);
                """;
        try (Connection connection = Util.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);

            preparedStatement.executeUpdate();
            System.out.printf("User с именем %s добавлен в базу данных\n", name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String SQL = """
                DELETE FROM simple.USER
                WHERE id = ?;
                """;
        try (Connection connection = Util.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public List<User> getAllUsers() {
            String SQL = """
                    SELECT *
                    FROM simple.USER;
                    """;
            try (Connection connection = Util.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<User> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(new User(
                            resultSet.getString("name"),
                            resultSet.getString("lastName"),
                            resultSet.getByte("age")
                    ));
                }
                System.out.println(result);
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();
    }
}
