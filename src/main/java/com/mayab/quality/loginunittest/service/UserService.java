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
		// Return null immediately if password length is invalid
		if (password.length() < 8 || password.length() > 16) {
			return null;
		}

		// Check if a user with the provided email already exists
		User existingUser = dao.findUserByEmail(email);
		if (existingUser != null) {
			return null;  // Return null if email is already used
		}
		
		// Proceed with creation if email is not duplicated
		User newUser = new User(name, email, password);
		int id = dao.save(newUser);
		newUser.setId(id);
		return newUser;
	}
	
	public List<User> findAllUsers(){
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
    	if (userOld != null) {
    	    userOld.setName(user.getName());
    	    userOld.setPassword(user.getPassword());
    	    return dao.updateUser(userOld);
    	}
    	return null;  // return null if the user to update is not found
    }

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }
}
