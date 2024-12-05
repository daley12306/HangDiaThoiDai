package vn.hangdiathoidai.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.hangdiathoidai.entity.Role;
import vn.hangdiathoidai.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
    private RoleRepository roleRepository;

    
    @Override
	public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
