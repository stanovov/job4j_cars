package ru.job4j.services.filter;

public class AdvertisementFilter implements Filter {

    private final ValueType vType;

    private final ConditionType cType;

    private final Object value;

    private final String pName;

    public AdvertisementFilter(ValueType vType, ConditionType cType,
                          Object value, String pName) {
        this.vType = vType;
        this.cType = cType;
        this.value = value;
        this.pName = pName;
    }

    @Override
    public ValueType getValueType() {
        return vType;
    }

    @Override
    public ConditionType getConditionType() {
        return cType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getParamName() {
        return pName;
    }
}
