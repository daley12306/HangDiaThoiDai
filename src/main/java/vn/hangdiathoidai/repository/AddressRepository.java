package vn.hangdiathoidai.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.hangdiathoidai.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUserId(Long userId);
	
	List<Address> findByUserId(Long userId, Sort sort);
}
