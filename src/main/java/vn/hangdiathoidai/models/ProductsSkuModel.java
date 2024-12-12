package vn.hangdiathoidai.models;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductsSkuModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private ProductModel product;
	private String sku;
	private Integer price;
	private Integer discount;
	private Integer stock;
	private Date createdAt;
	private Date updatedAt;

}
