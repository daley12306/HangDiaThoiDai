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

import jakarta.servlet.http.HttpServletRequest;
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
                               Model model, HttpSession session, HttpServletRequest request) {
        
        // Kiểm tra nếu page < 0 thì gán lại là 0
        if (page < 0) {
            page = 0;
        }

        // Tạo Pageable từ các tham số trang và kích thước
        Pageable pageable = PageRequest.of(page, size);
        
        // Tìm kiếm các Carrier theo từ khóa và phân trang
        Page<Carrier> carriers = carrierService.searchCarrier(keyword, pageable);

        // Nếu kết quả không đủ, chuyển về trang trước (nếu có)
        if (carriers.isEmpty() && page > 0) {
            page--;  // Chuyển về trang trước
            pageable = PageRequest.of(page, size);  // Tạo lại Pageable
            carriers = carrierService.searchCarrier(keyword, pageable);  // Tìm kiếm lại trên trang trước đó
        }

        // Lưu trang hiện tại vào session
        session.setAttribute("currentPage", page);

        // Thêm thông tin vào model để gửi tới view
        model.addAttribute("carriers", carriers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", carriers.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("currentUrl", request.getRequestURI());
        
        return "admin/carrier/list";  // Trả về view list.html
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
        int size = 10;
        int totalPages = (int) Math.ceil((double) totalCarriers / size);  // Tính tổng số trang
        
        if (totalCarriers == 0 || totalPages >= totalPages) {
            totalPages = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }
        
        // Redirect về trang cuối cùng
        return "redirect:/admin/carrier?page=" + totalPages + "&size=" + size; 
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

        long totalCarrier = carrierService.getTotalCarriers();  // Tổng số phần tử
        int size = 10;
        int totalPages = (int) Math.ceil((double) totalCarrier / size);  // Tính tổng số trang
        
        // Nếu số trang còn lại là 0, chuyển về trang đầu tiên
        if (totalCarrier == 0 || totalPages >= totalPages) {
            totalPages = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }

        // Quay lại trang đúng
        return "redirect:/admin/carrier?page=" + totalPages + "&size=" + size;  // Quay lại danh sách SubCategory
    }

}
