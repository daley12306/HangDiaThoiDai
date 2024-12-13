package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	AddressRepository addressRepository;
	
	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	@Override
	public void saveAddress(Address address) {
		addressRepository.save(address);
	}

	@Override
	public void deleteAddress(Long id) {
		addressRepository.deleteById(id);
	}

	@Override
	public Optional<Address> findById(Long id) {
		return addressRepository.findById(id);
	}

	@Override
	public List<Address> findByUserId(Long userId) {
		return addressRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "isDefault"));
	}

	@Override
	public List<Address> findAll() {
		return addressRepository.findAll();
	}

	@Override
	public void setDefaultAddress(Long id, Long userId) {
		List<Address> addresses = addressRepository.findByUserId(userId);
		for (Address address : addresses) {
			if (address.getId() == id) {
				address.setIsDefault(true);
			} else {
				address.setIsDefault(false);
			}
			addressRepository.save(address);
		}
	}
}
