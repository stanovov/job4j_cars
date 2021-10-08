package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "period_filters")
public class PeriodFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    public static PeriodFilter of(String name) {
        PeriodFilter periodFilter = new PeriodFilter();
        periodFilter.setName(name);
        return periodFilter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeriodFilter that = (PeriodFilter) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
