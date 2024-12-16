package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import vn.hangdiathoidai.entity.Role;

public interface RoleService {

	List<Role> findAllRoles();
	
	Optional<Role> findByRoleName(String roleName);
}
