package vn.hangdiathoidai.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private UserModel user;
	private Integer total;
	private Date createdAt;
	private Date updatedAt;
	private List<CartItemModel> cartItems;

}
