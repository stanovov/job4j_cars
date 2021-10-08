package ru.job4j.services.sort;

public class AdvertisementSort implements Sort {

    private final SortType sType;

    private final String fName;

    public AdvertisementSort(SortType sType, String fName) {
        this.sType = sType;
        this.fName = fName;
    }

    @Override
    public SortType getSortingType() {
        return sType;
    }

    @Override
    public String getFieldName() {
        return fName;
    }

}
