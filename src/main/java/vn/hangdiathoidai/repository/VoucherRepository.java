package vn.hangdiathoidai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hangdiathoidai.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
	Voucher findByCode(String code);
}
