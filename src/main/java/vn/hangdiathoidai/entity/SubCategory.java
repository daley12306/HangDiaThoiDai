package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Category parentCategory;
    
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String name;
    
    @OneToMany(mappedBy = "subCategory")
    private List<Product> product;
}
