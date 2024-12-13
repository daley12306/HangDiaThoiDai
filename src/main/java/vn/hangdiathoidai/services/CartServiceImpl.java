package vn.hangdiathoidai.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Cart;
import vn.hangdiathoidai.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	CartRepository cartRepository;
	
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
	public void deleteById(Long id) {
		cartRepository.deleteById(id);
	}

}
