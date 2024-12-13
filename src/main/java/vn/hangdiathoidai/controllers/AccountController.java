package vn.hangdiathoidai.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import vn.hangdiathoidai.entity.Address;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.models.AddressModel;
import vn.hangdiathoidai.models.UserModel;
import vn.hangdiathoidai.services.AddressService;
import vn.hangdiathoidai.services.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@GetMapping("")
	public String all() {
		return "redirect:/account/profile";
	}
	
	@GetMapping("/profile")
	public String profile(ModelMap model) {
		// Default user
		User user = userService.findUserById(2L);
		model.addAttribute("user", user);
		return "user/profile";
	}
	
	@PostMapping("/profile/save")
	public String saveProfile(@Valid @ModelAttribute("user") UserModel userModel,
			@RequestParam int day, @RequestParam int month, @RequestParam int year) throws ParseException {
		// Default user
		User user = userService.findUserById(2L);
		user.setUsername(userModel.getUsername());
		user.setFullName(userModel.getFullName());
		user.setEmail(userModel.getEmail());
		user.setPhoneNumber(userModel.getPhoneNumber());
		user.setBirthOfDate(new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + month + "-" + year));
		userService.saveUser(user);
		return "redirect:/account/profile";
	}
	
	@GetMapping("/address")
	public String address(ModelMap model, @RequestParam(required = false) Long selectedId) {
		List<Address> addresses = addressService.findByUserId(2L);
		model.addAttribute("addresses", addresses);
		
        if (selectedId != null) {
            Address selectedAddress = addressService.findById(selectedId)
                    .orElse(new Address());
            model.addAttribute("selectedAddress", selectedAddress);
        } else {
            model.addAttribute("selectedAddress", new Address());
        }
		
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
	public String saveAddress(@Valid @ModelAttribute("address") AddressModel addressModel) {
		Address address = new Address();
		BeanUtils.copyProperties(addressModel, address);
		address.setUser(userService.findUserById(2L));
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
	public String setDefaultAddress(@PathVariable Long id) {
		addressService.setDefaultAddress(id, 2L);
		return "redirect:/account/address";
	}
}
