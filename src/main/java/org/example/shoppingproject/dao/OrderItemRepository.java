package org.example.shoppingproject.dao;


import lombok.RequiredArgsConstructor;
import org.example.shoppingproject.domain.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class OrderItemRepository{

    private final SessionFactory sessionFactory;

    public List<OrderItem> findByOrderId(Long orderId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM OrderItem oi WHERE oi.order.id = :orderId";
        return session.createQuery(hql, OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void saveAll(List<OrderItem> items) {
        Session session = sessionFactory.getCurrentSession();
        int batchSize = 20; // flush & clear every 20 items to avoid memory issues

        for (int i = 0; i < items.size(); i++) {
            session.save(items.get(i));

            if (i > 0 && i % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }

        // flush remaining items
        session.flush();
        session.clear();
    }
}
