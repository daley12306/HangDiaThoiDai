package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.enums.CarrierStatus;
import vn.hangdiathoidai.repository.CarrierRepository;

@Service
public class CarrierServiceImpl implements CarrierService {
	@Autowired
    private CarrierRepository carrierRepository;

	// Thêm mới Carrier
    @Override
	public Carrier addCarrier(Carrier carrier) {
        return carrierRepository.save(carrier);
    }

    // Xóa Carrier
    @Override
	public void deleteCarrier(Long id) {
        carrierRepository.deleteById(id);
    }

    // Cập nhật Carrier
    @Override
	public Carrier updateCarrier(Long id, Carrier carrier) {
        if (carrierRepository.existsById(id)) {
            carrier.setId(id);
            return carrierRepository.save(carrier);
        }
        return null;
    }

    // Tìm kiếm Carrier với phân trang
    @Override
	public Page<Carrier> searchCarrier(String keyword, Pageable pageable) {
        return carrierRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
    }

    // Lấy Carrier theo id
    @Override
	public Optional<Carrier> getCarrierById(Long id) {
        return carrierRepository.findById(id);
    }

	@Override
	public long getTotalCarriers() {
		return carrierRepository.count();
	}
	
	@Override
	public List<Carrier> findActiveCarriersSortedByFee() {
        Sort sort = Sort.by(Sort.Order.asc("shippingFee"));
        return carrierRepository.findByStatus(CarrierStatus.ACTIVE, sort);
    }
}
