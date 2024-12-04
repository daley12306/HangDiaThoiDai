package vn.hangdiathoidai.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    private String avatar;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private Date birthOfDate;
    private String phoneNumber;
    private Date createdAt;


}
