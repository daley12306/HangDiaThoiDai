package vn.hangdiathoidai.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.models.AddressModel;
import vn.hangdiathoidai.models.UserModel;
import vn.hangdiathoidai.services.AddressService;
import vn.hangdiathoidai.services.FileStorageService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	
	@Autowired
	FileStorageService fileStorageService;

	
	@Value("${upload.dir}") 
    private String uploadDir;
	
	@GetMapping("")
	public String all() {
		return "redirect:/account/profile";
	}
	
	@GetMapping("/profile")
	public String profile(ModelMap model, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		return "user/profile";
	}
	
	@PostMapping("/profile/save")
	public String saveProfile(@Valid @ModelAttribute("user") UserModel userModel,
			@RequestParam int day, @RequestParam int month, @RequestParam int year,
			@AuthenticationPrincipal UserDetails userDetails, MultipartFile file) throws ParseException, IOException {
		User user = userService.findByUsername(userDetails.getUsername());
		user.setUsername(userModel.getUsername());
		user.setFullName(userModel.getFullName());
		user.setEmail(userModel.getEmail());
		user.setPhoneNumber(userModel.getPhoneNumber());
		user.setBirthOfDate(new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + month + "-" + year));
		
		User oldUrs = userService.findUserById(2L);
        String oldFile = oldUrs.getAvatar();
        if (file != null && !file.isEmpty()) {
            // Lưu ảnh mới nếu có
            String filename = fileStorageService.saveFile(file);  // Hàm này xử lý việc lưu ảnh
            user.setAvatar(filename);  // Cập nhật trường avatar với ảnh mới
        } else {
            // Nếu không thay đổi ảnh, giữ nguyên avatar cũ
            user.setAvatar(oldFile);
        }
		userService.saveUser(user);
		return "redirect:/account/profile";
	}
	
	@GetMapping("/address")
	public String address(ModelMap model, @RequestParam(required = false) Long selectedId,
			@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		List<Address> addresses = addressService.findByUserId(user.getId());
		model.addAttribute("addresses", addresses);
		return "user/address";
	}
	
	@GetMapping("/address/add")
	public String addAddress(ModelMap model) {
		model.addAttribute("address", new Address());
		return "user/address-edit";
	}
	
	@GetMapping("/address/edit/{id}")
	public String editAddress(@PathVariable Long id, ModelMap model) {
		Address address = addressService.findById(id).orElse(new Address());
		model.addAttribute("address", address);
		return "user/address-edit";
	}
	
	@PostMapping("/address/save")
	public String saveAddress(@Valid @ModelAttribute("address") AddressModel addressModel,
			@AuthenticationPrincipal UserDetails userDetails) {
		Address address = new Address();
		BeanUtils.copyProperties(addressModel, address);
		address.setUser(userService.findByUsername(userDetails.getUsername()));
		address.setIsDefault(false);
		addressService.saveAddress(address);
		return "redirect:/account/address";
	}
	
	@GetMapping("/address/delete/{id}")
	public String deleteAddress(@PathVariable Long id) {
		addressService.deleteAddress(id);
		return "redirect:/account/address";
	}
	
	@GetMapping("/address/set-default/{id}")
	public String setDefaultAddress(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		addressService.setDefaultAddress(id, user.getId());
		return "redirect:/account/address";
	}
}
