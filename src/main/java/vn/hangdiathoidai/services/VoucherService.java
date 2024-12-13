package vn.hangdiathoidai.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;

public interface VoucherService {

	Page<Voucher> searchVouchers(String keyword,DiscountType discountType, Pageable pageable);

	void deleteVoucher(Long id);

	Voucher getVoucherById(Long id);

	Voucher saveVoucher(Voucher voucher);

	boolean isVoucherValid(Long id, LocalDate currentDate);

	long getTotalVoucher();

}
