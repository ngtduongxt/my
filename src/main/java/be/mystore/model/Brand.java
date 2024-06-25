package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    private Image imageBrand;
}
