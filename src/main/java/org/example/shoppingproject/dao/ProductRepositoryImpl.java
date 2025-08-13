package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Product> findInStockProducts() {
        String hql = "FROM Product p WHERE p.quantity > 0";
        return getCurrentSession().createQuery(hql, Product.class).list();
    }


    @Override
    public List<Product> findPaginated(int pageNumber, int pageSize) {
        String hql = "FROM Product";
        Query<Product> query = getCurrentSession().createQuery(hql, Product.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }


    @Override
    public Product findById(Long id) {
        return getCurrentSession().get(Product.class, id);
    }

    @Override
    public void save(Product product) {
        getCurrentSession().save(product);
    }

    @Override
    public void update(Product product) {
        getCurrentSession().update(product);
    }
}

