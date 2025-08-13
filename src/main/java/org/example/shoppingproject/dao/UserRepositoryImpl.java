package org.example.shoppingproject.dao;


import org.example.shoppingproject.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User findByUsername(String username) {
        try {
            Query<User> query = getCurrentSession()
                    .createQuery("FROM User u WHERE u.username = :u", User.class);
            query.setParameter("u", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            Query<User> query = getCurrentSession()
                    .createQuery("FROM User u WHERE u.email = :e", User.class);
            query.setParameter("e", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            Query<User> query = getCurrentSession()
                    .createQuery("FROM User u WHERE u.username = :v OR u.email = :v", User.class);
            query.setParameter("v", usernameOrEmail);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        Session session = getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
            return user;
        } else {
            return (User) session.merge(user);
        }
    }
}
