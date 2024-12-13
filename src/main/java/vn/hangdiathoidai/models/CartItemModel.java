package vn.hangdiathoidai.models;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private CartModel cart;
	private ProductModel product;
	private ProductsSkuModel productsSku;
	private Integer quantity;
	private Date createdAt;
	private Date updatedAt;

}
