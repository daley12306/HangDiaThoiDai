package vn.hangdiathoidai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hangdiathoidai.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	List<CartItem> findByCartId(Long cartId);

}
