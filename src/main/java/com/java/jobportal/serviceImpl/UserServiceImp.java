package com.java.jobportal.serviceImpl;

import com.java.jobportal.model.Role;
import com.java.jobportal.model.User;
import com.java.jobportal.repository.UserRepository;
import com.java.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    @Override
    public Page<User> getUser(int page, int limit) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "name");
        Pageable pageable= PageRequest.of(page,limit,Sort.by(order));
        Page<User> userPage = userRepository.findByRoleNot(Role.ADMIN,pageable);
        return userPage;
    }

    @Override
    public User getById(Long id) throws Exception {

        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new Exception("Couldn't find Profile ");
    }

    @Override
    public void saveOrUpdate(User user) {
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void save(Long id , boolean disable) {
        userRepository.updateUserIsDisable(id, disable);
    }

    @Override
    public User getByUserNameOrEmail(String username,String email) {
        return userRepository.findByNameOrEmail(username,email);
    }
    
    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
    
    @Override
    public boolean isUserExists(String username) {
        return userRepository.existsByName(username);
    }

    @Override
    public boolean isNameOrEmailUnique(String name, String email) {
        return userRepository.findByNameOrEmail(name, email) != null;
    }

    @Override
    public boolean isNameUnique(String name) {
        return  userRepository.findByName(name) == null;
    }

    @Override
    public boolean isEmailUnique(String email) {
        return  userRepository.findByEmail(email) == null;
    }
    
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public void updatePassword(User userDetail, String newPassword) {
        String password=bCryptPasswordEncoder.encode(newPassword);
        userDetail.setPassword(password);
        userRepository.saveAndFlush(userDetail);
    }
}

