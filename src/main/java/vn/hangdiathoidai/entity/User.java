package vn.hangdiathoidai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String avatar;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String firstName;
    @Column(columnDefinition = "NVARCHAR(255) NOT NULL")
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Date birthOfDate;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String phoneNumber;
    private Date createdAt;
}
