package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.enums.OrderStatus;

public interface OrderDetailService {

	OrderDetail findById(Long id);

	Page<OrderDetail> findOrdersByCustomerName(String keyword, int page, int size);

	void updateOrderStatus(Long id, OrderStatus status);

	OrderDetail getOrderById(Long id);

	List<OrderDetail> getAllOrders();

	<S extends OrderDetail> S save(S entity);

	long getTotalOrders();

	Page<OrderDetail> findOrders(int page, int size);

	Page<OrderDetail> findOrdersByUserId(Long userId, int page, int size);

}
