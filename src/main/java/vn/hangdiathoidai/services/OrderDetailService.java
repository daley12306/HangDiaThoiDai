package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.enums.OrderStatus;

public interface OrderDetailService {

	void updateOrderStatus(Long id, OrderStatus status);

	OrderDetail getOrderById(Long id);

	List<OrderDetail> getAllOrders();

	Page<OrderDetail> findOrdersByCustomerName(String keyword, int page, int size);

	OrderDetail findById(Long id);

}
