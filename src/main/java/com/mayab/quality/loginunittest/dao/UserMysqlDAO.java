package com.mayab.quality.loginunittest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.mayab.quality.loginunittest.model.User;

public class UserMysqlDAO implements IDAOUser {

	public Connection getConnectionMySQL() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad2024", "root", "123456");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

	@Override
	public User findByUserName(String name) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		User result = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE name = ?");
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();

			// Check if there are any results before accessing the result set
			if (rs.next()) {
				int id = rs.getInt(1);
				String username  = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				boolean isLogged = rs.getBoolean(5);

				result = new User(username, password, email);
				result.setId(id);
				result.setLogged(isLogged);
			}

			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public int save(User user) {
		Connection connection = getConnectionMySQL();
		int result = -1;
		try {
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(
				"insert INTO usuarios(name,email,password,isLogged) values(?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS
			);

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setBoolean(4, user.isLogged());

			if (preparedStatement.executeUpdate() >= 1) {
				try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
					if (rs.next()) {
						result = rs.getInt(1);
					}
				}
			}

			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public User findUserByEmail(String email) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		User result = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE email = ?");
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();

			// Check if there are any results before accessing the result set
			if (rs.next()) {
				int id = rs.getInt(1);
				String username  = rs.getString(2);
				String emailUser = rs.getString(3);
				String password = rs.getString(4);
				boolean isLogged = rs.getBoolean(5);

				result = new User(username, password, emailUser);
				result.setId(id);
				result.setLogged(isLogged);
			}

			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public User findById(int id) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		User result = null;

		try {
			preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE id = ?");
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			// Check if there are any results before accessing the result set
			if (rs.next()) {
				int idUser = rs.getInt(1);
				String username  = rs.getString(2);
				String email = rs.getString(3);
				String password = rs.getString(4);
				boolean isLogged = rs.getBoolean(5);

				result = new User(username, password, email);
				result.setId(idUser);  // Using idUser as intended
				result.setLogged(isLogged);
			}

			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public boolean deleteById(int id) {
		Connection connection = getConnectionMySQL();
		boolean result = false;

		try {
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("Delete from usuarios WHERE id = ?");
			preparedStatement.setInt(1, id);

			if (preparedStatement.executeUpdate() >= 1) {
				result = true;
			}

			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public User updateUser(User userNew) {
		Connection connection = getConnectionMySQL();
		User result = null;

		try {
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(
				"UPDATE usuarios SET name = ?,password= ? WHERE id = ?"
			);
			preparedStatement.setString(1, userNew.getName());
			preparedStatement.setString(2, userNew.getPassword());
			preparedStatement.setInt(3, userNew.getId());

			if (preparedStatement.executeUpdate() >= 1) {
				result = userNew;
			}

			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
