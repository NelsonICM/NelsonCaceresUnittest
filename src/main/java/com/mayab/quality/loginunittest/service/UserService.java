package com.mayab.quality.loginunittest.service;

import java.util.List;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserService {
	
	private IDAOUser dao;
	
	public UserService(IDAOUser dao) {
		this.dao = dao;
	}
	
	public User createUser(String name, String username, String email, String password) {
		
		if (password.length() < 8 || password.length() > 16) {
			return null;
		}

		User existingUser = dao.findUserByEmail(email);
		if (existingUser != null) {
			return null;  
		}
		
		User newUser = new User(name, username, email, password);
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
    	    userOld.setUsername(user.getUsername());  // Asegura que se actualice el username también
    	    return dao.updateUser(userOld);
    	}
    	return null; 
    }

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }
}
