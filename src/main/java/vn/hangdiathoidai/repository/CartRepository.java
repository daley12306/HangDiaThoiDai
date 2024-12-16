package vn.hangdiathoidai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import vn.hangdiathoidai.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

	Optional<Cart> findByUserId(Long userId);
	
	@Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :id")
    void deleteByCartId(Long id);
}
