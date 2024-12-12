package vn.hangdiathoidai.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.repository.VoucherRepository;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;
	
	public VoucherServiceImpl(VoucherRepository voucherRepository) {
		this.voucherRepository = voucherRepository;
	}

	@Override
	public Voucher findByCode(String code) {
		return voucherRepository.findByCode(code);
	}

}
