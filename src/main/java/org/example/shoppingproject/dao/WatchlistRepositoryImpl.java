package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Product;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.domain.Watchlist;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class WatchlistRepositoryImpl implements WatchlistRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Product> findWatchlistProductsByUserId(Long userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Watchlist> root = cq.from(Watchlist.class);
        Join<Watchlist, Product> productJoin = root.join("product");
        cq.select(productJoin)
                .where(cb.equal(root.get("user").get("id"), userId))
                .distinct(true);

        return session.createQuery(cq).list();
    }

    @Override
    public void addProductToWatchlist(Long userId, Long productId) {
        Session session = getCurrentSession();
        String hql = "FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId";
        List<Watchlist> exists = session.createQuery(hql, Watchlist.class)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .list();

        if (exists.isEmpty()) {
            Watchlist w = new Watchlist();
            w.setUser(session.load(User.class, userId));
            w.setProduct(session.load(Product.class, productId));
            session.save(w);
        }
    }

    @Override
    public void removeProductFromWatchlist(Long userId, Long productId) {
        Session session = getCurrentSession();
        String hql = "DELETE FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId";
        session.createQuery(hql)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .executeUpdate();
    }
}
