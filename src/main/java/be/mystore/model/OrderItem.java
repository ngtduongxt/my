package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private OrderDetail orderDetail;
    @ManyToOne
    private Product product;

    private int quantity;
    private double unitPrice;
    private double subtotal;
}
