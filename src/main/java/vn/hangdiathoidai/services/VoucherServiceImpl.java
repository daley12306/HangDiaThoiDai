package vn.hangdiathoidai.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;
import vn.hangdiathoidai.repository.VoucherRepository;

@Service
public class VoucherServiceImpl implements VoucherService {
	 @Autowired
	    private VoucherRepository voucherRepository;

	    // Lưu voucher (thêm hoặc cập nhật)
	    @Override
		public Voucher saveVoucher(Voucher voucher) {
	        return voucherRepository.save(voucher);
	    }

	    // Lấy voucher theo id
	    @Override
		public Voucher getVoucherById(Long id) {
	        return voucherRepository.findById(id).orElse(null);
	    }

	    // Xóa voucher theo id
	    @Override
		public void deleteVoucher(Long id) {
	        voucherRepository.deleteById(id);
	    }

	    // Tìm kiếm và phân trang voucher
	    @Override
	    public Page<Voucher> searchVouchers(String keyword, DiscountType discountType, Pageable pageable) {
	        if (keyword != null && !keyword.isEmpty() || discountType != null) {
	            return voucherRepository.findByCodeContainingOrDescriptionContainingOrDiscountType(keyword, keyword, discountType, pageable);
	        } else {
	            return voucherRepository.findAll(pageable);
	        }
	    }


	    
	    @Override
		public boolean isVoucherValid(Long id, LocalDate currentDate) {
	        Voucher voucher = getVoucherById(id);
	        return voucher != null && voucher.isValid(currentDate);
	    }

		@Override
		public long getTotalVoucher() {
			return voucherRepository.count();
		}
	    
}
