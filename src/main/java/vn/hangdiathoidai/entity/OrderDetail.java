package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.OrderStatus;

import java.util.Date;

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
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentDetail payment;
    private Integer total; // Tổng tiền
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;  // Trạng thái đơn hàng
    @OneToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;
    @OneToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

}
