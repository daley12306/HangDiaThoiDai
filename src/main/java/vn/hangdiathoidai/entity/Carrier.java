package vn.hangdiathoidai.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.CarrierStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // Tên nhà vận chuyển (ví dụ: "Vietnam Post")
    @Email
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String email;
    @Column(nullable = false)
    private Integer shippingFee;  // Phí vận chuyển
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarrierStatus status;  // Trạng thái nhà vận chuyển (ACTIVE hoặc INACTIVE)


}
