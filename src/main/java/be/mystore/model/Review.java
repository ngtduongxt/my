package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    private int rating;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User reviewer;
    @OneToOne
    private Product product;
    private boolean isLike;
    private LocalDateTime lastUpdate;
}