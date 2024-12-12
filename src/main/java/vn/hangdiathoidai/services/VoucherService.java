package vn.hangdiathoidai.services;

import vn.hangdiathoidai.entity.Voucher;

public interface VoucherService {
	Voucher findByCode(String code);
}
