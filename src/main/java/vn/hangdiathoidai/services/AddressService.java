package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import vn.hangdiathoidai.entity.Address;

public interface AddressService {
	
	void saveAddress(Address address);

	void deleteAddress(Long id);

	Optional<Address> findById(Long id);

	List<Address> findByUserId(Long userId);

	List<Address> findAll();

	void setDefaultAddress(Long id, Long userId);
}
