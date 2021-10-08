package ru.job4j.store;

import ru.job4j.model.*;
import ru.job4j.services.filter.Filter;
import ru.job4j.services.sort.Sort;

import java.util.Collection;

public interface Store extends AutoCloseable {
    Collection<Brand> findAllBrands();
    Collection<Model> findModelsByBrand(Brand brand);
    Collection<BodyType> findAllBodyTypes();
    Collection<Transmission> findAllTransmissions();
    Collection<ProductionYear> findAllProductionYears();
    Collection<SortingType> findAllSortingTypes();
    Collection<PeriodFilter> findAllPeriodFilters();
    Collection<StatusFilter> findAllStatusFilters();
    Collection<Advertisement> findAdvertisements(Collection<Filter> filters, Sort sort);
    Advertisement findAdvertisementById(int id);
    void saveAdvertisement(Advertisement advertisement);
    void deleteAdvertisement(int id);
    User findUserByEmail(String email);
    void saveUser(User user);
}
