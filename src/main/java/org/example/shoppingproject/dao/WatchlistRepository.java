package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Watchlist;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

import java.util.List;

@Repository
public class WatchlistRepository {

    private final SessionFactory sessionFactory;

    public WatchlistRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Add to watchlist
    public void save(Watchlist watchlist) {
        Session session = sessionFactory.getCurrentSession();
        session.save(watchlist);
    }

    // Remove from watchlist
    public void deleteByUserAndProduct(Long userId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Query<?> query = session.createQuery(
                "DELETE FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId");
        query.setParameter("userId", userId);
        query.setParameter("productId", productId);
        query.executeUpdate();
    }

    // Get all in-stock products in watchlist
    public List<Watchlist> findInStockByUser(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Watchlist> query = session.createQuery(
                "FROM Watchlist w WHERE w.user.id = :userId AND w.product.quantity > 0", Watchlist.class);
        query.setParameter("userId", userId);
        return query.list();
    }

    // Check if exists
    public boolean existsByUserAndProduct(Long userId, Long productId) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(
                        "SELECT COUNT(w) FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId", Long.class)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .uniqueResult();
        return count != null && count > 0;
    }
}
