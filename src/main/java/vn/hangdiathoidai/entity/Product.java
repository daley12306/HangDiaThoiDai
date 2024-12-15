package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.ProductStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String name;
    @Column(columnDefinition = "TEXT NOT NULL")
    private String description;
    
    private String cover;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SubCategory subCategory;
    
    private Date createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProductsSku> productSkus = new ArrayList<>();
    
    @PrePersist
    public void prePersist() {
            this.createdAt = new Date();
            if (this.status == null) {  // Kiểm tra nếu trạng thái chưa được gán
                this.status = ProductStatus.INACTIVE;  // Gán mặc định là INACTIVE
            }
            if (productSkus == null || productSkus.isEmpty()) {
                ProductsSku newSku = new ProductsSku();
                newSku.setProduct(this);
                
                // Tạo SKU tùy chỉnh (ví dụ: kết hợp tên sản phẩm và một chuỗi ngẫu nhiên)
                String sku = generateSku();
                newSku.setSku(sku);
                
                newSku.setPrice(0); // Giá mặc định
                newSku.setQuantity(0); // Số lượng mặc định
                productSkus.add(newSku);
            }
            
        }
    private String generateSku() {
        // Lấy phần số từ UUID và cắt ra 8 ký tự đầu tiên
        String sku = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 12);
        return sku;
    }

}

