package vn.hangdiathoidai.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hangdiathoidai.entity.Carrier;

public interface CarrierService {

	Optional<Carrier> getCarrierById(Long id);

	Page<Carrier> searchCarrier(String keyword, Pageable pageable);

	Carrier updateCarrier(Long id, Carrier carrier);

	void deleteCarrier(Long id);

	Carrier addCarrier(Carrier carrier);

	long getTotalCarriers();

	

}
