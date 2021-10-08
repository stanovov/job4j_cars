package ru.job4j.services;

import ru.job4j.model.*;
import ru.job4j.services.sort.*;
import ru.job4j.store.AdRepository;
import ru.job4j.services.filter.*;


import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class AdvertisementService {

    /**
     * 1 - За всё время (без ограничений)
     * 2 - За сутки
     * 3 - За неделю
     * 4 - За месяц
     */
    private final Map<Integer, Supplier<Map<String, Date>>> periods = new HashMap<>();

    /**
     * sold = Продано (да/нет)
     * photo = Есть фото (да/нет)
     * brand = Марка (равенство)
     * bodyType = Тип кузова (равенство)
     * transmission = Коробка передач (равенство)
     * author = автор объявления (равенство)
     * stCreated = Начало периода
     * endCreated = Конец периода
     * stMileage = Пробег от
     * endMileage = Пробег до
     * stPrice = Цена от
     * endPrice = Цена до
     * stProdYear = Год от
     * endProdYear = Год до
     */
    private final Map<String, Function<Object, Filter>> filters = new HashMap<>();

    /**
     * 1 - По дате размещения
     * 2 - По возрастанию цены
     * 3 - По убыванию цены
     * 4 - По возрастанию пробега
     * 5 - По убыванию пробега
     */
    private final Map<Integer, Supplier<Sort>> sorts = new HashMap<>();

    private AdvertisementService() {
        initPeriods();
        initFilters();
        initSorts();
    }

    private static final class Lazy {
        private static final AdvertisementService INST = new AdvertisementService();
    }

    public static AdvertisementService instOf() {
        return AdvertisementService.Lazy.INST;
    }

    private void initPeriods() {
        periods.put(1, Map::of);
        periods.put(2, () -> {
            Calendar start = Calendar.getInstance();
            start.add(Calendar.DATE, -1);
            return Map.of("stCreated", start.getTime(), "endCreated", new Date());
        });
        periods.put(3, () -> {
            Calendar start = Calendar.getInstance();
            start.add(Calendar.DATE, -7);
            return Map.of("stCreated", start.getTime(), "endCreated", new Date());
        });
        periods.put(4, () -> {
            Calendar start = Calendar.getInstance();
            start.add(Calendar.MONTH, -1);
            return Map.of("stCreated", start.getTime(), "endCreated", new Date());
        });
    }

    private void initFilters() {
        filters.put("sold", v ->
                new AdvertisementFilter(ValueType.BOOLEAN, ConditionType.EQUAL, v, "sold"));
        filters.put("photo", v ->
                new AdvertisementFilter(ValueType.BOOLEAN, ConditionType.EQUAL, v, "photo"));
        filters.put("brand", v ->
                new AdvertisementFilter(ValueType.OBJECT, ConditionType.EQUAL, v, "model.brand"));
        filters.put("model", v ->
                new AdvertisementFilter(ValueType.OBJECT, ConditionType.EQUAL, v, "model"));
        filters.put("bodyType", v ->
                new AdvertisementFilter(ValueType.OBJECT, ConditionType.EQUAL, v, "bodyType"));
        filters.put("transmission", v ->
                new AdvertisementFilter(ValueType.OBJECT, ConditionType.EQUAL, v, "transmission"));
        filters.put("author", v ->
                new AdvertisementFilter(ValueType.OBJECT, ConditionType.EQUAL, v, "author"));
        filters.put("stCreated", v ->
                new AdvertisementFilter(ValueType.DATE, ConditionType.GREATER_OR_EQUAL, v, "created"));
        filters.put("endCreated", v ->
                new AdvertisementFilter(ValueType.DATE, ConditionType.LESS_OR_EQUAL, v, "created"));
        filters.put("stMileage", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.GREATER_OR_EQUAL, v, "mileage"));
        filters.put("endMileage", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.LESS_OR_EQUAL, v, "mileage"));
        filters.put("stPrice", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.GREATER_OR_EQUAL, v, "price"));
        filters.put("endPrice", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.LESS_OR_EQUAL, v, "price"));
        filters.put("stProdYear", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.GREATER_OR_EQUAL, v, "productionYear"));
        filters.put("endProdYear", v ->
                new AdvertisementFilter(ValueType.NUMERIC, ConditionType.LESS_OR_EQUAL, v, "productionYear"));
    }

    private void initSorts() {
        sorts.put(1, () -> new AdvertisementSort(SortType.DESC, "created"));
        sorts.put(2, () -> new AdvertisementSort(SortType.ASC,  "price"));
        sorts.put(3, () -> new AdvertisementSort(SortType.DESC, "price"));
        sorts.put(4, () -> new AdvertisementSort(SortType.ASC, "mileage"));
        sorts.put(5, () -> new AdvertisementSort(SortType.DESC, "mileage"));
    }

    public Collection<Advertisement> getAdvertisements(Map<String, String> params) {
        Map<String, Object> convertedParams = convertParameters(params);
        Collection<Filter> filters = getFilters(convertedParams);
        int sortId = params.containsKey("sortingType")
                ? Integer.parseInt(params.get("sortingType"))
                : 1;
        Sort sort = getSort(sortId);
        return AdRepository.instOf().findAdvertisements(filters, sort);
    }

    private Map<String, Object> convertParameters(Map<String, String> params) {
        Map<String, Object> convertedParams = new HashMap<>();
        convertPeriod(params, convertedParams);
        convertObjects(params, convertedParams);
        convertNumbers(params, convertedParams);
        convertBooleans(params, convertedParams);
        return convertedParams;
    }

    private void convertPeriod(Map<String, String> params, Map<String, Object> convertedParams) {
        int periodId = params.containsKey("periodFilter")
                ? Integer.parseInt(params.get("periodFilter"))
                : 1;
        convertedParams.putAll(
                getPeriod(periodId)
        );
    }

    private void convertObjects(Map<String, String> params, Map<String, Object> convertedParams) {
        if (params.containsKey("model")) {
            Model model = new Model();
            model.setId(Integer.parseInt(params.get("model")));
            convertedParams.put("model", model);
        } else if (params.containsKey("brand")) {
            Brand brand = new Brand();
            brand.setId(Integer.parseInt(params.get("brand")));
            convertedParams.put("brand", brand);
        }
        if (params.containsKey("bodyType")) {
            BodyType bodyType = new BodyType();
            bodyType.setId(Integer.parseInt(params.get("bodyType")));
            convertedParams.put("bodyType", bodyType);
        }
        if (params.containsKey("transmission")) {
            Transmission transmission = new Transmission();
            transmission.setId(Integer.parseInt(params.get("transmission")));
            convertedParams.put("transmission", transmission);
        }
        if (params.containsKey("author")) {
            User user = new User();
            user.setId(Integer.parseInt(params.get("author")));
            convertedParams.put("author", user);
        }
    }

    private void convertNumbers(Map<String, String> params, Map<String, Object> convertedParams) {
        if (params.containsKey("stProdYear") && params.containsKey("endProdYear")) {
            int stProdYear = Integer.parseInt(params.get("stProdYear"));
            int endProdYear = Integer.parseInt(params.get("endProdYear"));
            int start = Math.min(stProdYear, endProdYear);
            int end = Math.max(stProdYear, endProdYear);
            convertedParams.put("stProdYear", start);
            convertedParams.put("endProdYear", end);
        } else if (params.containsKey("stProdYear")) {
            convertedParams.put("stProdYear", Integer.parseInt(params.get("stProdYear")));
        } else if (params.containsKey("endProdYear")) {
            convertedParams.put("endProdYear", Integer.parseInt(params.get("endProdYear")));
        }
        if (params.containsKey("stMileage") && params.containsKey("endMileage")) {
            int stMileage = Integer.parseInt(params.get("stMileage"));
            int endMileage = Integer.parseInt(params.get("endMileage"));
            int start = Math.min(stMileage, endMileage);
            int end = Math.max(stMileage, endMileage);
            convertedParams.put("stMileage", start);
            convertedParams.put("endMileage", end);
        } else if (params.containsKey("stMileage")) {
            convertedParams.put("stMileage", Integer.parseInt(params.get("stMileage")));
        } else if (params.containsKey("endMileage")) {
            convertedParams.put("endMileage", Integer.parseInt(params.get("endMileage")));
        }
        if (params.containsKey("stPrice") && params.containsKey("endPrice")) {
            double stPrice = Double.parseDouble(params.get("stPrice"));
            double endPrice = Double.parseDouble(params.get("endPrice"));
            double start = Math.min(stPrice, endPrice);
            double end = Math.max(stPrice, endPrice);
            convertedParams.put("stPrice", start);
            convertedParams.put("endPrice", end);
        } else if (params.containsKey("stPrice")) {
            convertedParams.put("stPrice", Double.parseDouble(params.get("stPrice")));
        } else if (params.containsKey("endPrice")) {
            convertedParams.put("endPrice", Double.parseDouble(params.get("endPrice")));
        }
    }

    private void convertBooleans(Map<String, String> params, Map<String, Object> convertedParams) {
        if (params.containsKey("photo")) {
            convertedParams.put("photo", Boolean.parseBoolean(params.get("photo")));
        }
        if (params.containsKey("statusFilter")) {
            int statusId = Integer.parseInt(params.get("statusFilter"));
            if (statusId == 2) {
                convertedParams.put("sold", true);
            } else if (statusId == 3) {
                convertedParams.put("sold", false);
            }
        } else if (params.containsKey("sold")) {
            convertedParams.put("sold", Boolean.parseBoolean(params.get("sold")));
        }
    }

    private Map<String, Date> getPeriod(int id) {
        Supplier<Map<String, Date>> s = periods.get(id);
        if (s == null) {
            throw new IllegalArgumentException("Incorrect period id");
        }
        return s.get();
    }

    private Collection<Filter> getFilters(Map<String, Object> map) {
        List<Filter> result = new ArrayList<>();
        for (String k : map.keySet()) {
            Filter f = getFilter(k, map.get(k));
            result.add(f);
        }
        return result;
    }

    private Filter getFilter(String k, Object v) {
        Function<Object, Filter> f = filters.get(k);
        if (f == null) {
            throw new IllegalArgumentException("Incorrect key");
        }
        return f.apply(v);
    }

    public Sort getSort(int id) {
        Supplier<Sort> s = sorts.get(id);
        if (s == null) {
            throw new IllegalArgumentException("Incorrect sort type id");
        }
        return s.get();
    }
}
