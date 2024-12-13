package vn.hangdiathoidai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hangdiathoidai.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUserId(Long userId);
	
}
