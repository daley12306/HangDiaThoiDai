package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String number;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String street;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String district;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String province;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String ward;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String phoneNumber;
    @Column(columnDefinition = "BIT DEFAULT 0 NOT NULL")
    private Boolean isDefault;

}
