package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private float prince;
    private int quantity;

    @OneToOne
    private Category category;
    private String discretion;

    @OneToMany
    private Set<Image> imageProduct = new HashSet<>();
    @ManyToOne
    private Brand brand;
    private Date lastUpdate;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @OneToMany
    private List<Review> reviewer = new ArrayList<>();
}