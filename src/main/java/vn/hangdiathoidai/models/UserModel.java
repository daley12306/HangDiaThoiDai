package vn.hangdiathoidai.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
    private String password;
    private String confirmPassword;
    private Date birthOfDate;
    private String phoneNumber;
    private Date createdAt;
    private Boolean terms;
    
    
}
