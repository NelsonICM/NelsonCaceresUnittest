package com.mayab.quality.integration;

import java.io.FileInputStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import java.sql.Connection;
import java.sql.DriverManager;

class UserDAOTest {

    UserMysqlDAO daoMySql;

    public UserDAOTest() {
       
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad2024");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456");
    }

    @BeforeEach
    void setUp() throws Exception {
        daoMySql = new UserMysqlDAO();
        
      
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
          
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        } catch (Exception e) {
            e.printStackTrace();  
            Assertions.fail("Error en la configuración inicial: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private IDatabaseConnection getConnection() throws Exception {
        try {
            
            Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/calidad2024", "root", "123456");
            return new DatabaseConnection(jdbcConnection);
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private IDataSet getDataSet() throws Exception {
       
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/resources/initDB.xml"));
    }

    @Test
    void testAddUserQry() {
       
        String plainPassword = "123456";
        User usuario = new User("username2", "correo2@correo.com", plainPassword);

        int newID = daoMySql.save(usuario);

        try {
            IDatabaseConnection conn = getConnection();
            QueryDataSet actualTable = new QueryDataSet(conn);
            actualTable.addTable("insertTMP", "SELECT * FROM usuarios WHERE id = " + newID);

            String actualUsername = (String) actualTable.getTable("insertTMP").getValue(0, "username");
            String actualEmail = (String) actualTable.getTable("insertTMP").getValue(0, "email");
            String actualPassword = (String) actualTable.getTable("insertTMP").getValue(0, "password");

            assertThat(actualUsername, is(usuario.getUsername()));
            assertThat(actualEmail, is(usuario.getEmail()));
            assertThat(actualPassword, is(plainPassword)); 
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en el test de inserción: " + e.getMessage());
        }
    }
}
