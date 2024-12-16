package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.OrderItem;
import vn.hangdiathoidai.repository.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	@Autowired
    private OrderItemRepository orderItemRepository;

    @Override
	public List<OrderItem> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }
	@Override
	public <S extends OrderItem> S save(S entity) {
		return orderItemRepository.save(entity);
	}
    
    
}
