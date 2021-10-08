package ru.job4j.services;

import ru.job4j.services.filter.*;
import ru.job4j.services.sort.Sort;
import ru.job4j.services.sort.SortType;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;

public class HbmQueryBuilder<T> {

    private final CriteriaBuilder cb;

    private final Root<T> root;

    public HbmQueryBuilder(CriteriaBuilder cb, Root<T> root) {
        this.cb = cb;
        this.root = root;
    }

    public Predicate[] where(Collection<Filter> filters) {
        int i = 0;
        Predicate[] predicates = new Predicate[filters.size()];
        for (Filter filter : filters) {
            String pName = filter.getParamName();
            ConditionType cType = filter.getConditionType();
            ValueType vType = filter.getValueType();
            Object v = filter.getValue();
            Predicate p = null;
            if (vType == ValueType.OBJECT) {
                p = whereObj(v, cType, pName);
            } else if (vType == ValueType.NUMERIC) {
                p = whereNumber(v, cType, pName);
            } else if (vType == ValueType.DATE) {
                p = whereDate(v, cType, pName);
            } else if (vType == ValueType.BOOLEAN) {
                p = whereBoolean(v, cType, pName);
            }
            if (p == null) {
                throw new IllegalArgumentException("Incorrect value type");
            }
            predicates[i++] = p;
        }
        return predicates;
    }

    public Order orderBy(Sort sort) {
        SortType sType = sort.getSortingType();
        String fName = sort.getFieldName();
        Order order;
        if (sType == SortType.ASC) {
            order = cb.asc(root.get(fName));
        } else if (sType == SortType.DESC) {
            order = cb.desc(root.get(fName));
        } else {
            throw new IllegalArgumentException("Incorrect sorting type");
        }
        return order;
    }

    private Predicate whereObj(Object v, ConditionType cType, String pName) {
        Predicate predicate;
        if (cType == ConditionType.EQUAL) {
            String[] path = pName.split("\\.");
            Path p = root.get(path[0]);
            for (int i = 1; i < path.length; i++) {
                p = p.get(path[i]);
            }
            predicate = cb.equal(p, v);
        } else {
            throw new IllegalArgumentException("Incorrect condition type");
        }
        return predicate;
    }

    private Predicate whereNumber(Object v, ConditionType cType, String pName) {
        Number n = (Number) v;
        Predicate predicate;
        if (cType == ConditionType.GREATER_OR_EQUAL) {
            predicate = cb.ge(root.get(pName), n);
        } else if (cType == ConditionType.EQUAL) {
            predicate = cb.equal(root.get(pName), n);
        } else if (cType == ConditionType.LESS_OR_EQUAL) {
            predicate = cb.le(root.get(pName), n);
        } else {
            throw new IllegalArgumentException("Incorrect condition type");
        }
        return predicate;
    }

    private Predicate whereDate(Object v, ConditionType cType, String pName) {
        Date d = (Date) v;
        Predicate predicate;
        if (cType == ConditionType.GREATER_OR_EQUAL) {
            predicate = cb.greaterThanOrEqualTo(root.get(pName), d);
        } else if (cType == ConditionType.EQUAL) {
            predicate = cb.equal(root.get(pName), d);
        } else if (cType == ConditionType.LESS_OR_EQUAL) {
            predicate = cb.lessThanOrEqualTo(root.get(pName), d);
        } else {
            throw new IllegalArgumentException("Incorrect condition type");
        }
        return predicate;
    }

    private Predicate whereBoolean(Object v, ConditionType cType, String pName) {
        Predicate predicate;
        if (cType == ConditionType.EQUAL) {
            predicate = cb.equal(root.get(pName), v);
        } else {
            throw new IllegalArgumentException("Incorrect condition type");
        }
        return predicate;
    }
}
