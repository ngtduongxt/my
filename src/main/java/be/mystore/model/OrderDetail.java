package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Order order;

    private String shippingAddress;
    private String paymentMethod;
}
