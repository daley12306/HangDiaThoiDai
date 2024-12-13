package vn.hangdiathoidai.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Voucher;
import vn.hangdiathoidai.enums.DiscountType;
import vn.hangdiathoidai.services.VoucherService;


@Controller
@RequestMapping("admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    // Trang danh sách voucher với phân trang và tìm kiếm
    @GetMapping
    public String listVouchers(@RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               Model model, HttpSession session) {
        Pageable pageable = PageRequest.of(page, 10);

        // Kiểm tra nếu người dùng nhập vào loại giảm giá (discountType)
        DiscountType discountTypeEnum = null;
        if (keyword != null && !keyword.isEmpty()) {
            try {
                // Nếu giá trị keyword có thể chuyển thành DiscountType, thì tìm theo loại giảm giá
                discountTypeEnum = DiscountType.valueOf(keyword.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Nếu không phải DiscountType hợp lệ, coi như là tìm kiếm theo mã và mô tả
                discountTypeEnum = null;
            }
        }
        

        // Tìm kiếm các voucher với keyword và discountType (nếu có)
        Page<Voucher> vouchers = voucherService.searchVouchers(keyword, discountTypeEnum, pageable);
        session.setAttribute("currentPage", page);
        model.addAttribute("vouchers", vouchers);
        model.addAttribute("keyword", keyword);
        return "admin/voucher/list";
    }


    // Trang thêm voucher
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "admin/voucher/add";
    }

    // Lưu voucher (Thêm mới)
    @PostMapping("/add")
    public String saveVoucher(@ModelAttribute("voucher") Voucher voucher) {
        voucherService.saveVoucher(voucher);
     // Tính toán trang cuối cùng
        long totalVoucher = voucherService.getTotalVoucher();  // Lấy tổng số nhà vận chuyển
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalVoucher / size);  // Tính tổng số trang

        // Nếu tổng số phần tử chia hết cho size, trang cuối cùng là totalPages - 1
        if (totalVoucher % size == 0 && totalVoucher > 0) {
            totalPages--;  // Điều chỉnh trang cuối cùng nếu chia hết
        }
        return "redirect:/admin/voucher?page=" + (totalPages-1) + "&size=" + size;  // Chuyển hướng về trang cuối;
    }

    // Trang chỉnh sửa voucher
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Voucher voucher = voucherService.getVoucherById(id);
        model.addAttribute("voucher", voucher);
        return "admin/voucher/edit";
    }

    // Cập nhật voucher
    @PostMapping("/update")
    public String updateVoucher(@ModelAttribute("voucher") Voucher voucher, HttpSession session) {
        voucherService.saveVoucher(voucher);
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/voucher?page="+page;
    }

    // Xóa voucher
    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable("id") Long id, HttpSession session) {
        voucherService.deleteVoucher(id);
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/voucher?page="+page;
    }
   
    
}