package vn.hangdiathoidai.models;


import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Id;
import lombok.*;
import vn.hangdiathoidai.enums.DiscountType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherModel implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    private String code;  // Mã voucher (ví dụ: "DISCOUNT10")
    private String description;  // Mô tả voucher
    private DiscountType discountType;
    private Float discountValue;  // Giá trị giảm giá - tính theo phần trăm
    private Integer minOrderValue;  // Giá trị tối thiểu của đơn hàng để sử dụng voucher
    private Date startDate;  // Ngày bắt đầu hiệu lực
    private Date endDate;    // Ngày hết hạn
    private Integer quantity;  // Số lượng voucher còn lại
}
