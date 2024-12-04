package vn.hangdiathoidai.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.micrometer.common.util.StringUtils;
import vn.hangdiathoidai.entity.User;
import vn.hangdiathoidai.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	    private UserRepository userRepository;

}

		

