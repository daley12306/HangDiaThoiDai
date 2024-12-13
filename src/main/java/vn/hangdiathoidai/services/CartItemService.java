package vn.hangdiathoidai.services;

import java.util.List;

import vn.hangdiathoidai.entity.CartItem;

public interface CartItemService {
	List<CartItem> findByCartId(Long cartId);
	
	<S extends CartItem> S save(S entity);
	
	void deleteById(Long id);
	
	CartItem findById(Long id);
}
