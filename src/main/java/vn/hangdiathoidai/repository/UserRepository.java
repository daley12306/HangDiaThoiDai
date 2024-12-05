package vn.hangdiathoidai.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.hangdiathoidai.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
    
    // Tìm kiếm user theo tên hoặc email, phân trang
    Page<User> findByFullNameContainingOrEmailContaining(String fullName, String email, Pageable pageable);
}
