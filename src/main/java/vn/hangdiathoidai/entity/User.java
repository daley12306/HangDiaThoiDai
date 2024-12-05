package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "avatar")
    private String avatar;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String fullName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthOfDate;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String phoneNumber;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    @ManyToOne
    @JoinColumn(name = "role_id")  // Ngoại khóa tới bảng roles
    private Role role; // Trường role trong bảng user
    
    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
