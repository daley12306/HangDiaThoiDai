package vn.hangdiathoidai.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Carrier;
import vn.hangdiathoidai.services.CarrierService;

@Controller
@RequestMapping("/admin/carrier")  // Đường dẫn chính là /admin/carrier
public class CarrierController {

    @Autowired
    private CarrierService carrierService;

    // Hiển thị danh sách Carrier với phân trang và tìm kiếm
    @GetMapping
    public String listCarriers(@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword,
                               Model model, HttpSession session) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Carrier> carriers = carrierService.searchCarrier(keyword, pageable);
        
        session.setAttribute("currentPage", page);
        
        model.addAttribute("carriers", carriers);
        model.addAttribute("keyword", keyword);
        return "admin/carrier/list";  // Đường dẫn đến file list.html là admin/carrier/list
    }

    // Mở form thêm Carrier
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("carrier", new Carrier());
        return "admin/carrier/add";  // Đường dẫn đến file add.html là admin/carrier/add
    }

    // Thêm mới Carrier
    @PostMapping("/add")
    public String addCarrier(@ModelAttribute Carrier carrier, HttpSession session) {
        carrierService.addCarrier(carrier);  // Thêm nhà vận chuyển mới

        // Tính toán trang cuối cùng
        long totalCarriers = carrierService.getTotalCarriers();  // Lấy tổng số nhà vận chuyển
        int size = 10;  // Số lượng phần tử mỗi trang (có thể lấy từ tham số hoặc mặc định)
        int totalPages = (int) Math.ceil((double) totalCarriers / size);  // Tính tổng số trang

        // Nếu tổng số phần tử chia hết cho size, trang cuối cùng là totalPages - 1
        if (totalCarriers % size == 0 && totalCarriers > 0) {
            totalPages--;  // Điều chỉnh trang cuối cùng nếu chia hết
        }

        // Redirect về trang cuối cùng
        return "redirect:/admin/carrier?page=" + (totalPages-1) + "&size=" + size;  // Chuyển hướng về trang cuối
    }


    // Mở form sửa Carrier
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        carrierService.getCarrierById(id).ifPresent(carrier -> model.addAttribute("carrier", carrier));
        return "admin/carrier/edit";  // Đường dẫn đến file edit.html là admin/carrier/edit
    }

    // Cập nhật Carrier
    @PostMapping("/edit/{id}")
    public String updateCarrier(@PathVariable Long id, @ModelAttribute Carrier carrier, HttpSession session) {
        carrierService.updateCarrier(id, carrier);
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/carrier?page="+page;  // Điều hướng về danh sách Carrier
    }

    // Xóa Carrier
    @GetMapping("/delete/{id}")
    public String deleteCarrier(@PathVariable Long id, HttpSession session) {
        carrierService.deleteCarrier(id);  // Xóa nhà vận chuyển

        // Lấy trang hiện tại từ session
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }

        return "redirect:/admin/carrier?page=" + page;  // Redirect về trang hiện tại
    }

}
