package com.mayab.quality.loginunittest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mayab.quality.loginunittest.model.User;

public class UserMysqlDAO implements IDAOUser {

    public Connection getConnectionMySQL() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad2024", "root", "123456");
        } catch (Exception e) {
            System.out.println("Error en la conexión a MySQL: " + e);
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
            preparedStatement = connection.prepareStatement("SELECT * from usuarios WHERE username = ?");
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, password, email);
                result.setId(id);
                result.setLogged(isLogged);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println("Error en findByUserName: " + e);
        }
        return result;
    }

    @Override
    public int save(User user) throws Exception {
        Connection connection = getConnectionMySQL();
        int result = -1;

        // Verificar si el correo electrónico ya existe
        if (findUserByEmail(user.getEmail()) != null) {
            throw new Exception("El correo electrónico ya está registrado");
        }

        // Validar la longitud de la contraseña
        if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
            throw new Exception("La contraseña debe tener entre 6 y 20 caracteres.");
        }

        if (connection == null) {
            System.out.println("Conexión a MySQL fallida.");
            return result;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO usuarios (username, email, password, isLogged) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.isLogged());

            if (preparedStatement.executeUpdate() >= 1) {
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        result = rs.getInt(1);  // Obtiene el ID generado
                    }
                }
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Error al guardar usuario: " + e);
            throw e; // Relanzar la excepción para manejarla fuera del método
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

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String emailUser = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, emailUser, password);
                result.setId(id);
                result.setLogged(isLogged);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println("Error en findUserByEmail: " + e);
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

            if (rs.next()) {
                int idUser = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new User(username, email, password);
                result.setId(idUser);  
                result.setLogged(isLogged);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println("Error en findById: " + e);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        Connection connection = getConnectionMySQL();
        boolean result = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from usuarios WHERE id = ?");
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() >= 1) {
                result = true;
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Error en deleteById: " + e);
        }
        return result;
    }

    @Override
    public User updateUser(User userNew) {
        Connection connection = getConnectionMySQL();
        User result = null;

        try {
            // Verificar si el username existe antes de actualizar
            User existingUser = findById(userNew.getId());
            if (existingUser != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE usuarios SET username = ?, password = ?, email = ?, isLogged = ? WHERE id = ?"
                );
                preparedStatement.setString(1, userNew.getUsername());
                preparedStatement.setString(2, userNew.getPassword());
                preparedStatement.setString(3, userNew.getEmail());
                preparedStatement.setBoolean(4, userNew.isLogged());
                preparedStatement.setInt(5, userNew.getId());

                if (preparedStatement.executeUpdate() >= 1) {
                    result = userNew;
                }

                preparedStatement.close();
            }

            connection.close();

        } catch (Exception e) {
            System.out.println("Error en updateUser: " + e);
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        Connection connection = getConnectionMySQL();
        PreparedStatement preparedStatement;
        ResultSet rs;
        List<User> users = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * from usuarios");
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                User user = new User(username, email, password);
                user.setId(id);
                user.setLogged(isLogged);
                users.add(user);
            }

            connection.close();
            rs.close();
            preparedStatement.close();

        } catch (Exception e) {
            System.out.println("Error en findAll: " + e);
        }
        return users;
    }
}
