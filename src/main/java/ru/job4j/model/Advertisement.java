package ru.job4j.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(nullable = false)
    private boolean sold;

    @Column(nullable = false)
    private boolean photo;

    @Column(nullable = false)
    private int mileage;

    @Column(precision = 10, scale = 2, nullable = false)
    private double price;

    @Column(name = "production_year", nullable = false)
    private int productionYear;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name = "body_type_id", nullable = false)
    private BodyType bodyType;

    @ManyToOne
    @JoinColumn(name = "transmission_id", nullable = false)
    private Transmission transmission;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public static Advertisement of(String description, Model model, BodyType bodyType,
                                   Transmission transmission, User author) {
        Advertisement advertisement = new Advertisement();
        advertisement.setDescription(description);
        advertisement.setCreated(new Date(System.currentTimeMillis()));
        advertisement.setModel(model);
        advertisement.setBodyType(bodyType);
        advertisement.setTransmission(transmission);
        advertisement.setAuthor(author);
        return advertisement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Advertisement advertisement = (Advertisement) o;
        return id == advertisement.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Advertisement{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", created=" + created
                + ", sold=" + sold
                + ", photo=" + photo
                + ", mileage=" + mileage
                + ", price=" + price
                + ", model=" + model
                + ", bodyType=" + bodyType
                + ", transmission=" + transmission
                + ", productionYear=" + productionYear
                + ", author=" + author
                + '}';
    }
}
