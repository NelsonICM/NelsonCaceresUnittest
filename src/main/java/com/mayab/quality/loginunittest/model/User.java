package com.mayab.quality.loginunittest.model;

public class User {
	private int id;
	private String name;
	private String username; 
	private String email;
	private String password;
	private boolean isLogged;
	
	public User(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.isLogged = false;
	}
	
	// Constructor actualizado para incluir 'username'
	public User(String username, String email, String password) {
		this.username = username; // Inicializa 'username' directamente
		this.email = email;
		this.password = password;
		this.isLogged = false;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isLogged() {
		return isLogged;
	}
	
	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", email=" + email + ", password=" + password + ", isLogged=" + isLogged + "]";
	}
}
