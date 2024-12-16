package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import vn.hangdiathoidai.entity.*;
import vn.hangdiathoidai.repository.CartItemRepository;
import vn.hangdiathoidai.repository.CartRepository;
import vn.hangdiathoidai.repository.ProductRepository;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	CartRepository cartRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CartItemRepository cartItemRepository;
	
	public CartServiceImpl(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	@Override
	public Optional<Cart> findByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}

	@Override
	public <S extends Cart> S save(S entity) {
		return cartRepository.save(entity);
	}

	@Override
	public void deleteByCartId(Long id) {
		cartRepository.deleteByCartId(id);
	}


	@Override
	@Transactional
	public void addToCart(Long productId, Long userId, ProductsSku sku, Integer quantity) {
		// Get or create cart for user
		Cart cart = cartRepository.findByUserId(userId)
				.orElseGet(() -> {
					Cart newCart = new Cart();
					User user = new User();
					user.setId(userId);
					newCart.setUser(user);
					newCart.setTotal(0);
					return cartRepository.save(newCart);
				});

		// Find existing cart item with same product SKU
		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
		CartItem existingItem = cartItems.stream()
				.filter(item -> item.getProductsSku().getId().equals(sku.getId()))
				.findFirst()
				.orElse(null);

		if (existingItem != null) {
			// Update existing cart item
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
			existingItem.setUpdatedAt(new Date());
			cartItemRepository.save(existingItem);
		} else {
			// Create new cart item
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product not found")));
			newItem.setProductsSku(sku);
			newItem.setQuantity(quantity);
			newItem.setCreatedAt(new Date());
			newItem.setUpdatedAt(new Date());
			cartItemRepository.save(newItem);
		}

		// Update cart total
		int newTotal = cartItems.stream()
				.mapToInt(item -> item.getProductsSku().getPrice() * item.getQuantity())
				.sum();
		cart.setTotal(newTotal);
		cartRepository.save(cart);
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));
	}

}
