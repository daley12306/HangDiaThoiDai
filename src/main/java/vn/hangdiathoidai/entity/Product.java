package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hangdiathoidai.enums.ProductStatus;

import java.util.Date;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    private String cover;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SubCategory subCategory;
    
    private Date createdAt;
    
    @PrePersist
    public void prePersist() {
            this.createdAt = new Date();
        }
}

