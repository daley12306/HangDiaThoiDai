package vn.hangdiathoidai.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.enums.CarrierStatus;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
	Page<Carrier> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

	List<Carrier> findByStatus(CarrierStatus status, Sort sort);
}
