package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.OrderStatus;
import vn.hangdiathoidai.enums.PaymentMethod;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Integer total; // Tổng tiền
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;  // Trạng thái đơn hàng
    @Temporal(TemporalType.TIMESTAMP)
    
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @OneToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
}
