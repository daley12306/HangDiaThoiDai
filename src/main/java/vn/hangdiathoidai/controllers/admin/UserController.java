package vn.hangdiathoidai.controllers.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hangdiathoidai.entity.Role;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.services.FileStorageService;
import vn.hangdiathoidai.services.RoleService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {
	
	@Autowired
	private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Value("${upload.dir}") // Đường dẫn thư mục upload được cấu hình trong application.properties
    private String uploadDir;

    
 // Hiển thị danh sách user với phân trang
    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String searchTerm, 
                            @RequestParam(value = "page", defaultValue = "0") int page, 
                            Model model, HttpSession session, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> users;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            users = userService.searchUsers(searchTerm, pageable);
        } else {
            users = userService.searchUsers("", pageable);
        }
        session.setAttribute("currentPage", page);
        model.addAttribute("users", users);
        model.addAttribute("search", searchTerm);
        model.addAttribute("currentPage", page);
        
        model.addAttribute("currentUrl", request.getRequestURI());
        
        return "admin/user/list";  
    }
    
    
    // Thêm user
    @GetMapping("/create")
    public String createUserForm(Model model) {
    	List<Role> roles = roleService.findAllRoles();
    	model.addAttribute("roles", roles);
        model.addAttribute("user", new User());
        return "admin/user/create";  // Đảm bảo có trang "create.html"
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute User user, 
                           @RequestParam("file") MultipartFile file,
                           @RequestParam(defaultValue = "0") int page,  // Nhận tham số trang
                           @RequestParam(defaultValue = "10") int size, HttpSession session) {  // Số lượng mỗi trang

        if (!file.isEmpty()) {
            try {
                // Lưu ảnh như đã làm trước đây
                String uploadDirectory = uploadDir + File.separator;
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDirectory + File.separator + fileName);
                Files.write(path, file.getBytes());
                user.setAvatar(fileName);  // Lưu đường dẫn ảnh vào trường avatar của User
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userService.saveUser(user);  // Lưu người dùng vào cơ sở dữ liệu

        Integer currentPage = (Integer) session.getAttribute("currentPage");
        if (currentPage == null) {
            currentPage = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }

        // Kiểm tra số lượng SubCategory còn lại sau khi xóa
        long totalUser = userService.getTotalUsers();  // Tổng số phần tử
        int totalPages = (int) Math.ceil((double) totalUser / size);  // Tính tổng số trang
        
        // Nếu số trang còn lại là 0, chuyển về trang đầu tiên
        if (totalUser == 0 || currentPage >= totalPages) {
            currentPage = Math.max(totalPages - 1, 0);  // Nếu không còn phần tử hoặc trang hiện tại vượt quá tổng số trang, quay lại trang trước đó
        }

        // Quay lại trang đúng
        return "redirect:/admin/users?page=" + currentPage + "&size=" + size;  // Quay lại danh sách SubCategory
    }

    

    // Trang chỉnh sửa người dùng
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
    	List<Role> roles = roleService.findAllRoles();
    	model.addAttribute("roles", roles);
        User user = userService.findUserById(id);
        if (user == null) {
            return "redirect:/admin/users"; // Nếu không tìm thấy người dùng, quay lại trang danh sách
        }
        model.addAttribute("user", user);
        return "admin/user/edit";  // Trả về trang edit.html
    }

    // Xử lý form chỉnh sửa người dùng
    @PostMapping("/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            @ModelAttribute User user,
            @RequestParam(value = "file", required = false) MultipartFile file, HttpSession session
    ) throws IOException {
        User existingUser = userService.findUserById(id);  // Lấy user hiện tại từ DB
        String oldFile = existingUser.getAvatar();  // Lấy tên ảnh cũ
        // Nếu không tải ảnh mới lên, giữ lại ảnh cũ
        if (file != null && !file.isEmpty()) {
            // Lưu ảnh mới nếu có
        	String filename = fileStorageService.saveFile(file);
        	user.setAvatar(filename);
        }else {
            // Nếu không thay đổi ảnh, giữ nguyên avatar cũ
            user.setAvatar(oldFile);
        }

        // Cập nhật thông tin người dùng trong DB
        user.setId(id);  // Đảm bảo ID đúng
        userService.saveUser(user);
        
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        return "redirect:/admin/users?page="+page;  // Redirect về trang danh sách người dùng
    }

    // Xóa user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        userService.deleteUser(id);
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) {
            page = 0;  // Nếu không có trang hiện tại, mặc định là trang đầu tiên
        }
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/admin/users?page=" + page;  // Redirect về trang hiện tại";
    }
    
    


}
