package vn.hangdiathoidai.models;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String description;
	private String image;
	private Integer price;
	private Integer quantity;
	private Date createdAt;
	private Date updatedAt;

}
