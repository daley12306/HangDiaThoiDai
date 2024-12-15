package vn.hangdiathoidai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findByOrder_Id(Long orderDetailId);

}
