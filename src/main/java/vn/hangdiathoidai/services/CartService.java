package vn.hangdiathoidai.services;

import java.util.Optional;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.entity.ProductsSku;

public interface CartService {
	Optional<Cart> findByUserId(Long userId);
	
	<S extends Cart> S save(S entity);
	
	void deleteByCartId(Long id);

	//void addToCart(Long userId, Long productId, Integer quantity);

	void addToCart(Long productId, Long id, ProductsSku sku, Integer quantity);

	Cart getCartByUserId(Long userId);
}
