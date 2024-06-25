package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "\"Order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @ManyToOne
    private User users;
    @OneToMany
    private List<OrderDetail> orderDetails;
    private LocalDateTime orderDate;
    private double totalAmount;
}
