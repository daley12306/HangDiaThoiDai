package vn.hangdiathoidai.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>{
	Page<Voucher> findByCodeContainingOrDescriptionContaining(String code, String description, Pageable pageable);
    Page<Voucher> findByDiscountType(DiscountType discountType, Pageable pageable);
    Page<Voucher> findByCodeContainingOrDescriptionContainingOrDiscountType(String code, String description, DiscountType discountType, Pageable pageable);
}
