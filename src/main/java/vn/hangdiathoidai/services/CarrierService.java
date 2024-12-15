package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.enums.CarrierStatus;

public interface CarrierService {

	Optional<Carrier> getCarrierById(Long id);

	Page<Carrier> searchCarrier(String keyword, Pageable pageable);

	Carrier updateCarrier(Long id, Carrier carrier);

	void deleteCarrier(Long id);

	Carrier addCarrier(Carrier carrier);

	long getTotalCarriers();

	List<Carrier> findActiveCarriersSortedByFee();

}
