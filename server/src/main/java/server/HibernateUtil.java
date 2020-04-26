package server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.Session.*;

public class HibernateUtil {
    private static SessionFactory sessionFactory = buildSessionFactory();

    public HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory() {
        try {
            if (sessionFactory == null) {
                Configuration configuration = new Configuration()
                        .configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
                // TODO: manually add entity classes
                configuration.addAnnotatedClass(UserInfo.class);
                configuration.addAnnotatedClass(Game.class);
                StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
                serviceRegistryBuilder.applySettings(configuration.getProperties());
                ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }
            return sessionFactory;
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void addUserInfo(UserInfo u) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(u);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void addGame(Game g){
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(g);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static boolean addUserInfoIfNone(UserInfo u) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        boolean add = false;

        try {
            tx = session.beginTransaction();
            List<UserInfo> user = session.createQuery("SELECT u FROM UserInfo u " + "where u.username like :name")
                    .setParameter("name", (String) u.getUsername()).list();
            if (user.size() == 0) {
                add = true;
                session.save(u);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return add;
    }

    public static List<UserInfo> getUserInfoList() {
        Session session = sessionFactory.openSession();
        List<UserInfo> users = session.createQuery("SELECT a FROM UserInfo a", UserInfo.class).getResultList();
        session.close();
        return users;
    }

    public static List<Game> getGameList() {
        Session session = sessionFactory.openSession();
        List<Game> games = session.createQuery("SELECT a FROM Game a", Game.class).getResultList();
        session.close();
        return games;
    }

    public static void updateUserInfo(UserInfo user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void updateGame(Game g) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(g);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void deleteGame(Game g){
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(g);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}