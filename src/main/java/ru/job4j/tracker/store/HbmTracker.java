package ru.job4j.tracker.store;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public void init() {

    }

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.close();
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        item.setId(id);
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(int id) {
        Item item = new Item(null);
        item.setId(id);
        boolean result = false;
        Session session = sf.openSession();
        session.beginTransaction();
        session.delete(item);
        result = session.contains(item);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Item> itemCriteria = cb.createQuery(Item.class);
        Root<Item> itemRoot = itemCriteria.from(Item.class);
        itemCriteria.select(itemRoot);
        List result = session.createQuery(itemCriteria).getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Item> itemCriteria = cb.createQuery(Item.class);
        Root<Item> itemRoot = itemCriteria.from(Item.class);
        itemCriteria.select(itemRoot);
        itemCriteria.where(cb.equal(itemRoot.get("name"), key));
        List result = session.createQuery(itemCriteria).getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findById(int id){
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}