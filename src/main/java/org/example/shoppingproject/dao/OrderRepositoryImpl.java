package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.dto.RecentItemDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        String hql = "FROM Order o WHERE o.user.id = :userId";
        return getCurrentSession()
                .createQuery(hql, Order.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public Order findByIdAndUserId(Long orderId, Long userId) {
        String hql = "FROM Order o WHERE o.id = :orderId AND o.user.id = :userId";
        try {
            return getCurrentSession()
                    .createQuery(hql, Order.class)
                    .setParameter("orderId", orderId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override

    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(getCurrentSession().get(Order.class, orderId));
    }

    @Override
    public List<RecentItemDto> findRecentItemsByUser(Long userId, int limit) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT new org.example.shoppingproject.dto.RecentItemDto(" +
                "oi.productId, " +
                "oi.productName, " +
                "MAX(o.placedAt), " +
                "COUNT(oi.id)) " +
                "FROM OrderItem oi " +
                "JOIN oi.order o " +
                "WHERE o.user.id = :userId " +
                "AND o.status <> :canceled " +
                "GROUP BY oi.productId, oi.productName " +
                "ORDER BY MAX(o.placedAt) DESC, oi.productId ASC";

        return session.createQuery(hql, RecentItemDto.class)
                .setParameter("userId", userId)
                .setParameter("canceled", Order.Status.CANCELED)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void save(Order order) {
        getCurrentSession().save(order);
    }

    @Override
    public void update(Order order) {
        getCurrentSession().update(order);
    }

}
