package vn.hangdiathoidai.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;

	// Thêm user
    @Override
	public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Sửa user
    @Override
	public User updateUser(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setFullName(userDetails.getFullName());
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setBirthOfDate(userDetails.getBirthOfDate());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setRole(userDetails.getRole());
            return userRepository.save(existingUser);
        }
        return null;
    }

    // Xóa user
    @Override
	public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Tìm kiếm và phân trang
    @Override
	public Page<User> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.findByFullNameContainingOrEmailContaining(searchTerm, searchTerm, pageable);
    }
    @Override
	public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

	@Override
	public long getTotalUsers() {
		return userRepository.count();
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}

		

