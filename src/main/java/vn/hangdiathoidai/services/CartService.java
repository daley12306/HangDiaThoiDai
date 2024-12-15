package vn.hangdiathoidai.services;

import java.util.Optional;

import vn.hangdiathoidai.entity.Cart;

public interface CartService {
	Optional<Cart> findByUserId(Long userId);
	
	<S extends Cart> S save(S entity);
	
	void deleteByCartId(Long id);
}
