package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/simple";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static Connection connection;
    public static Session session;
    private static SessionFactory sessionFactory;

    private Util() {
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static Session getSession() {
        Configuration configuration = new Configuration().addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        return session;
    }

    public static void closeSession() {
        if (sessionFactory != null) {
            if (session != null) {
                session.close();
            }
            sessionFactory.close();
        }
    }
}
