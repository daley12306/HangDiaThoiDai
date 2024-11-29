package vn.hangdiathoidai.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.PaymentStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail order;
    @Column(nullable = false)
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Date createdAt;
    private Date updatedAt;
}
