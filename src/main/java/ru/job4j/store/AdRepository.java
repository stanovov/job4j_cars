package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Advertisement;
import ru.job4j.model.Brand;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AdRepository implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(AdRepository.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private AdRepository() {
    }

    private static final class Lazy {
        private static final AdRepository INST = new AdRepository();
    }

    public static AdRepository instOf() {
        return Lazy.INST;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public Collection<Advertisement> findForLastDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        List<Advertisement> list = new ArrayList<>();
        String hql = "SELECT a FROM Advertisement a "
                    + "WHERE a.created BETWEEN :stDate AND current_timestamp ";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Advertisement.class)
                    .setParameter("stDate", c.getTime())
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND FOR LAST DAY");
        }
        return list;
    }

    public Collection<Advertisement> findWithPhoto() {
        List<Advertisement> list = new ArrayList<>();
        String hql = "SELECT a FROM Advertisement a "
                    + "WHERE a.photo = true ";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Advertisement.class)
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND WITH PHOTO");
        }
        return list;
    }

    public Collection<Advertisement> findByBrand(Brand brand) {
        List<Advertisement> list = new ArrayList<>();
        String hql = "SELECT a FROM Advertisement a "
                    + "WHERE a.brand = :pBrand";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Advertisement.class)
                    .setParameter("pBrand", brand)
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND WITH PHOTO");
        }
        return list;
    }

    private <T> T executeTransaction(Function<Session, T> f) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T result = f.apply(session);
            tx.commit();
            return result;
        } catch (final Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
