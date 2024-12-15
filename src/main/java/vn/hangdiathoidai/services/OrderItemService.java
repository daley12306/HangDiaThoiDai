package vn.hangdiathoidai.services;

import java.util.List;

import vn.hangdiathoidai.entity.OrderItem;

public interface OrderItemService {

	List<OrderItem> getItemsByOrderId(Long orderId);

}
