package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final UserDaoHibernateImpl INSTANCE = new UserDaoHibernateImpl();
    public static UserDaoHibernateImpl getInstance(){
        return INSTANCE;
    }


    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String sql = """
                CREATE TABLE users ( 
                id BIGSERIAL PRIMARY KEY , 
                name TEXT NOT NULL , 
                lastname TEXT NOT NULL , 
                age SMALLINT NOT NULL )
                """;
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Table [users] created");
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Exception. Fail to create [users] TABLE");
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = """
                DROP TABLE users
                """;

        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Table [users] dropped");
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Exception. Table may not exist");
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users(name, lastName, age)" +
                     "VALUES (?, ?, ?)";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User: " + name + " " + lastName + " " + age + " added.");
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("User: " + name + " " + lastName + " " + age + " saved");
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                System.out.println("User " + id + "(id) has been removed");
            }else {
                System.out.println("user not exist");
            }
        }catch (Exception e){
            transaction.rollback();
            System.out.println("Exception.User cant be remove");
        }finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String hql = """
                FROM User 
                """;
        Session session = Util.getSessionFactory().openSession();
        List<User> listUsers = null;
        try{
            listUsers = session.createQuery(hql,User.class).list();
        }catch (Exception e){
            System.out.println("Exception. Error getting all users");
        }finally {
            session.close();
        }
        return listUsers;
    }

    @Override
    public void cleanUsersTable() {
        String sql = """
                TRUNCATE TABLE users
                """;
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Table [users] cleared");
        }catch (Exception e) {
            transaction.rollback();
            System.out.println("Exception. Table [users] cant be cleared");
        } finally {
            session.close();
        }
    }


}
