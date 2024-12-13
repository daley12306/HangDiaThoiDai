package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.hangdiathoidai.enums.DiscountType;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;  // Mã voucher (ví dụ: "DISCOUNT10")
    @Column(columnDefinition = "TEXT")
    private String description;  // Mô tả voucher
    @Enumerated(EnumType.STRING)
    
    @Column(nullable = false)
    private DiscountType discountType;
    
    @Column(nullable = false)
    private Float discountValue;  // Giá trị giảm giá - tính theo phần trăm
    @Column(nullable = false)
    private Integer minOrderValue;  // Giá trị tối thiểu của đơn hàng để sử dụng voucher
    @Column(nullable = false)
    private LocalDate startDate;  // Ngày bắt đầu hiệu lực
    @Column(nullable = false)
    private LocalDate endDate;    // Ngày hết hạn
    @Column(nullable = false)
    private Integer quantity;  // Số lượng voucher còn lại
    
    public boolean isValid(LocalDate currentDate) {
        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate) && quantity > 0;
    }
}
