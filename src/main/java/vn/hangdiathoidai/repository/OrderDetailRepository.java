package vn.hangdiathoidai.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.hangdiathoidai.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	Page<OrderDetail> findByUser_FullNameContainingIgnoreCase(String keyword, Pageable pageable);

}
