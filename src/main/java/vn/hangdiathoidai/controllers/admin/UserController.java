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
                            Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> users;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            users = userService.searchUsers(searchTerm, pageable);
        } else {
            users = userService.searchUsers("", pageable);
        }
        model.addAttribute("users", users);
        model.addAttribute("search", searchTerm);
        return "admin/user/list";  // Đảm bảo có trang "list.html" trong thư mục templates
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
    public String saveUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file) {
    	
    	if (!file.isEmpty()) {
            try {
                // Tạo đường dẫn lưu trữ ảnh
                String uploadDirectory = uploadDir + File.separator + "avatars"; 
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lấy tên ảnh
                String fileName = file.getOriginalFilename();
                Path path = Paths.get(uploadDirectory + File.separator + fileName);

                // Lưu ảnh vào thư mục
                Files.write(path, file.getBytes());
                
                // Lưu đường dẫn ảnh vào trường avatar của User
                user.setAvatar("/uploads/"+fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lưu user vào cơ sở dữ liệu
        userService.saveUser(user);

        // Redirect đến trang danh sách người dùng
        return "redirect:/admin/users";
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
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute User user,
            @RequestParam("file") MultipartFile file
    ) {
        // Nếu có ảnh mới được tải lên
        if (!file.isEmpty()) {
            try {
                // Tạo thư mục lưu ảnh nếu chưa tồn tại
                String uploadDirectory = uploadDir + File.separator + "avatars";
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    dir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lấy tên file và lưu ảnh vào thư mục
                String fileName = file.getOriginalFilename();
                Path path = Paths.get(uploadDirectory, fileName);
                file.transferTo(path); // Lưu file

                // Cập nhật trường avatar trong user
                user.setAvatar("/uploads/" + fileName); // Đường dẫn ảnh sẽ được lưu trong DB
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Cập nhật thông tin người dùng
        userService.saveUser(user);
        return "redirect:/admin/users";  // Quay lại trang danh sách người dùng
    }
    // Xóa user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/admin/users";
    }
}
