package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.CartItem;
import vn.hangdiathoidai.repository.CartItemRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	CartItemRepository cartItemRepository;
	
	public CartItemServiceImpl(CartItemRepository cartItemRepository) {
		this.cartItemRepository = cartItemRepository;
	}
	
	@Override
	public List<CartItem> findByCartId(Long cartId) {
		return cartItemRepository.findByCartId(cartId);
	}

	@Override
	public <S extends CartItem> S save(S entity) {
		return cartItemRepository.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		cartItemRepository.deleteById(id);
	}

	@Override
	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).orElse(null);
	}

}
