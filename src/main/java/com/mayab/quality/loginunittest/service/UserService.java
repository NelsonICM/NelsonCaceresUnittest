package com.mayab.quality.loginunittest.service;

import java.util.List;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserService {

    private IDAOUser dao;

    public UserService(IDAOUser dao) {
        this.dao = dao;
    }

    public User createUser(String name, String email, String password) {
        if (password.length() >= 8 && password.length() <= 16) {
            User user = dao.findUserByEmail(email);
            if (user == null) {
                user = new User(name, email, password);
                int id = dao.save(user);
                user.setId(id);
            }
            return user;
        }
        return null;
    }

    public List<User> findAllUsers() {
        return dao.findAll();
    }

    public User findUserByEmail(String email) {
        return dao.findUserByEmail(email);
    }

    public User findUserById(int id) {
        return dao.findById(id);
    }

    public User updateUser(User user) {
        User userOld = dao.findById(user.getId());
        userOld.setName(user.getName());
        userOld.setPassword(user.getPassword());
        return dao.updateUser(userOld);
    }

    public boolean deleteUser(int id) {
        return dao.deleteById(id);
    }
}
