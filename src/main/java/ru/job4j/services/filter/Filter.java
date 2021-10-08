package ru.job4j.services.filter;

public interface Filter {
    ValueType getValueType();
    ConditionType getConditionType();
    Object getValue();
    String getParamName();
}
