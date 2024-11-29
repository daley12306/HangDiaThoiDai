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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Liên kết với bảng người dùng

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Liên kết với bảng sản phẩm

    @Column(nullable = false)
    private Integer rating;  // Điểm đánh giá (1-5)

    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String reviewText;  // Nội dung đánh giá

    private Date createdAt;
    private Date updatedAt;
}
