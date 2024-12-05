package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.hangdiathoidai.entity.User;

public interface UserService {

	Page<User> searchUsers(String searchTerm, Pageable pageable);

	void deleteUser(Long id);

	User updateUser(Long id, User userDetails);

	User saveUser(User user);

	User findUserById(Long id);
	
}
