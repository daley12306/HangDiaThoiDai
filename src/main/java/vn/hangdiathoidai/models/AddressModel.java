package vn.hangdiathoidai.models;

import java.io.Serializable;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressModel implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private UserModel user;
    private String number;
    private String street;
    private String district;
    private String province;
    private String ward;
    private String phoneNumber;
    private Boolean isDefault;
}
