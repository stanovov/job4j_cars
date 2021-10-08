package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.*;
import ru.job4j.services.HbmQueryBuilder;
import ru.job4j.services.filter.Filter;
import ru.job4j.services.sort.Sort;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AdRepository implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(AdRepository.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private AdRepository() {
    }

    private static final class Lazy {
        private static final Store INST = new AdRepository();
    }

    public static Store instOf() {
        return Lazy.INST;
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

    @Override
    public Collection<Brand> findAllBrands() {
        List<Brand> list = new ArrayList<>();
        String hql = "SELECT b FROM Brand b ORDER BY b.name";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Brand.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find brands", e);
        }
        return list;
    }

    @Override
    public Collection<Model> findModelsByBrand(Brand brand) {
        List<Model> list = new ArrayList<>();
        String hql = "SELECT m FROM Model m WHERE m.brand = :pBrand ORDER BY m.name";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Model.class)
                    .setParameter("pBrand", brand)
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find models by brand", e);
        }
        return list;
    }

    @Override
    public Collection<BodyType> findAllBodyTypes() {
        List<BodyType> list = new ArrayList<>();
        String hql = "SELECT b FROM BodyType b ORDER BY b.id";
        try {
            list = executeTransaction(session -> session.createQuery(hql, BodyType.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find body types", e);
        }
        return list;
    }

    @Override
    public Collection<Transmission> findAllTransmissions() {
        List<Transmission> list = new ArrayList<>();
        String hql = "SELECT b FROM Transmission b ORDER BY b.id";
        try {
            list = executeTransaction(session -> session.createQuery(hql, Transmission.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find transmissions", e);
        }
        return list;
    }

    @Override
    public Collection<ProductionYear> findAllProductionYears() {
        List<ProductionYear> list = new ArrayList<>();
        String hql = "SELECT b FROM ProductionYear b ORDER BY b.year DESC";
        try {
            list = executeTransaction(session -> session.createQuery(hql, ProductionYear.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find production years", e);
        }
        return list;
    }

    @Override
    public Collection<SortingType> findAllSortingTypes() {
        List<SortingType> list = new ArrayList<>();
        String hql = "SELECT b FROM SortingType b ORDER BY b.id";
        try {
            list = executeTransaction(session -> session.createQuery(hql, SortingType.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find sorting types", e);
        }
        return list;
    }

    @Override
    public Collection<PeriodFilter> findAllPeriodFilters() {
        List<PeriodFilter> list = new ArrayList<>();
        String hql = "SELECT b FROM PeriodFilter b ORDER BY b.id";
        try {
            list = executeTransaction(session -> session.createQuery(hql, PeriodFilter.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find period filters", e);
        }
        return list;
    }

    @Override
    public Collection<StatusFilter> findAllStatusFilters() {
        List<StatusFilter> list = new ArrayList<>();
        String hql = "SELECT b FROM StatusFilter b ORDER BY b.id";
        try {
            list = executeTransaction(session -> session.createQuery(hql, StatusFilter.class).list());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find status filters", e);
        }
        return list;
    }

    @Override
    public Collection<Advertisement> findAdvertisements(Collection<Filter> filters, Sort sort) {
        List<Advertisement> list = new ArrayList<>();
        try {
            list = executeTransaction(session -> {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Advertisement> cr = cb.createQuery(Advertisement.class);
                Root<Advertisement> root = cr.from(Advertisement.class);
                HbmQueryBuilder<Advertisement> builder = new HbmQueryBuilder<>(cb, root);
                Predicate[] predicates = builder.where(filters);
                Order order = builder.orderBy(sort);
                cr = cr.select(root)
                        .where(predicates)
                        .orderBy(order);
                return session.createQuery(cr).list();
            });
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find ads", e);
        }
        return list;
    }

    @Override
    public Advertisement findAdvertisementById(int id) {
        Advertisement result = null;
        try {
            result = executeTransaction(session -> session.get(Advertisement.class, id));
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find advertisement by id", e);
        }
        return result;
    }

    @Override
    public void saveAdvertisement(Advertisement advertisement) {
        try {
            executeTransaction(session -> {
                if (advertisement.getId() == 0) {
                    session.save(advertisement);
                } else {
                    session.update(advertisement);
                }
                return true;
            });
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't save advertisement", e);
        }
    }

    @Override
    public void deleteAdvertisement(int id) {
        try {
            executeTransaction(session -> {
                session.delete(session.get(Advertisement.class, id));
                return true;
            });
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't delete advertisement", e);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        User result = null;
        String hql = "SELECT u FROM User u WHERE u.email = :pEmail";
        try {
            result = executeTransaction(session -> session.createQuery(hql, User.class)
                    .setParameter("pEmail", email)
                    .uniqueResult());
        } catch (Exception e) {
            LOG.error("Database query failed. Couldn't find user by email", e);
        }
        return result;
    }

    @Override
    public void saveUser(User user) {
        try {
            try {
                executeTransaction(session -> {
                    session.save(user);
                    return true;
                });
            } catch (ConstraintViolationException ignored) {
            }
        } catch (Exception e) {
            LOG.error("Database query failed. Failed to save user", e);
        }
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
