package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.OrderDetail;
import vn.hangdiathoidai.enums.OrderStatus;
import vn.hangdiathoidai.repository.OrderDetailRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	
	@Autowired
    private OrderDetailRepository orderDetailRepository;
	
	 @Override
	public List<OrderDetail> getAllOrders() {
	        return orderDetailRepository.findAll();
	    }

	    @Override
		public OrderDetail getOrderById(Long id) {
	        return orderDetailRepository.findById(id).orElse(null);
	    }

	    @Override
		public void updateOrderStatus(Long id, OrderStatus status) {
	        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
	        if (orderDetail != null) {
	            orderDetail.setStatus(status);
	            orderDetailRepository.save(orderDetail);
	        }
	    }

		@Override
		public Page<OrderDetail> findOrdersByCustomerName(String keyword, int page, int size) {
			Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

	        if (keyword == null || keyword.isEmpty()) {
	            // Nếu không có từ khóa, trả về tất cả đơn hàng
	            return orderDetailRepository.findAll(pageable);
	        } else {
	            // Tìm kiếm theo tên khách hàng
	            return orderDetailRepository.findByUser_FullNameContainingIgnoreCase(keyword, pageable);
	        }
	    }

		@Override
		public OrderDetail findById(Long id) {
			return orderDetailRepository.findById(id).orElse(null);
		}

		@Override
		public <S extends OrderDetail> S save(S entity) {
			return orderDetailRepository.save(entity);
		}
}
