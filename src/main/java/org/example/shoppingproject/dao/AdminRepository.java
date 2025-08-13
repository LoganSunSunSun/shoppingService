package org.example.shoppingproject.dao;


import org.example.shoppingproject.domain.Order;
import org.example.shoppingproject.domain.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AdminRepository {

    private final SessionFactory sessionFactory;

    public AdminRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // 1. Paginated orders for admin using Criteria
    public List<Order> findOrdersForAdmin(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        root.fetch("user", JoinType.LEFT); // eager fetch user info
        cq.select(root).orderBy(cb.desc(root.get("placedAt")));
        Query<Order> query = session.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    // 2. List all products
    public List<Product> getAllProducts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Product", Product.class).getResultList();
    }

    // 3. Get product by ID
    public Product getProductById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Product.class, id);
    }

    // 4. Save / update product
    public Product saveOrUpdateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
        return product;
    }

    // 5. Save / update order
    public Order saveOrUpdateOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(order);
        return order;
    }

    // 6. Total items sold
    public Long totalSoldItems() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(oi.quantity) from OrderItem oi join oi.order o where o.status='COMPLETED'";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long result = query.uniqueResult();
        return result != null ? result : 0L;
    }

    // 7. Top 3 products by units sold
    public List<Object[]> top3ProductsByUnitsSold() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select oi.productId, sum(oi.quantity) as total " +
                "from OrderItem oi join oi.order o " +
                "where o.status='COMPLETED' " +
                "group by oi.productId " +
                "order by total desc";
        return session.createQuery(hql, Object[].class)
                .setMaxResults(3)
                .getResultList();
    }

    // 8. Top profit product
    public Object[] topProfitProduct() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select oi.productId, sum((oi.retailAtPurchase - oi.wholesaleAtPurchase) * oi.quantity) as profit " +
                "from OrderItem oi join oi.order o " +
                "where o.status='COMPLETED' " +
                "group by oi.productId " +
                "order by profit desc";
        return session.createQuery(hql, Object[].class)
                .setMaxResults(1)
                .uniqueResult();
    }
}
