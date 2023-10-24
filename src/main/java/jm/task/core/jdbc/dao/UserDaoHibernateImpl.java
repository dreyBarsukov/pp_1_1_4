package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private Session session = Util.getSession();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String SQL_CREATE = """
                CREATE TABLE IF NOT EXISTS USER (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(128),
                    lastName VARCHAR(128),
                    age TINYINT);
                """;
        try {
            session.beginTransaction();
            session.createNativeQuery(SQL_CREATE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        String SQL_DROP = """
                DROP TABLE IF EXISTS USER;
                """;
        try {
            session.beginTransaction();
            session.createNativeQuery(SQL_DROP).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = null;

        try {
            session.beginTransaction();
            result = session.createQuery("from User").getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        String sqlClean = """
                DELETE FROM USER;
                """;
        try {
            session.beginTransaction();
            session.createNativeQuery(sqlClean).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }
}
