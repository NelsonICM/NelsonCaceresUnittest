package com.mayab.quality.integration;

import java.io.FileInputStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.commons.dbcp2.BasicDataSource;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import javax.sql.DataSource;

class UserDAOTest {

    private UserMysqlDAO daoMySql;
    private DatabaseDataSourceConnection dbConnection;

    public UserDAOTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad2024");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456");
    }

    private DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/calidad2024");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @BeforeEach
    void setUp() throws Exception {
        daoMySql = new UserMysqlDAO();
        dbConnection = new DatabaseDataSourceConnection(getDataSource());

        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbConnection, getDataSet());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en la configuración inicial: " + e.getMessage());
        }
    }

    private IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @AfterEach
    void tearDown() throws Exception {
        if (dbConnection != null) {
            try {
                DatabaseOperation.DELETE_ALL.execute(dbConnection, getDataSet());
            } finally {
                dbConnection.close();
            }
        }
    }

    // 1. createUser tests
    @Test
    void testCreateUserHappyPath() throws Exception {
        User usuario = new User("username2", "correo2@correo.com", "123456");
        int newID = daoMySql.save(usuario);

        try {
            QueryDataSet actualTable = new QueryDataSet(dbConnection);
            actualTable.addTable("insertTMP", "SELECT * FROM usuarios WHERE id = " + newID);

            assertThat(actualTable.getTable("insertTMP").getValue(0, "username"), is(usuario.getUsername()));
            assertThat(actualTable.getTable("insertTMP").getValue(0, "email"), is(usuario.getEmail()));
            assertThat(actualTable.getTable("insertTMP").getValue(0, "password"), is("123456"));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en el test de inserción: " + e.getMessage());
        }
    }

    @Test
    void testCreateUserWithDuplicateEmail() throws Exception {
        User usuario = new User("username1", "correo1@correo.com", "validPassword");
        daoMySql.save(usuario);

        User usuarioDuplicado = new User("username2", "correo1@correo.com", "validPassword");
        Assertions.assertThrows(Exception.class, () -> daoMySql.save(usuarioDuplicado));
    }

    @Test
    void testCreateUserWithInvalidPassword() {
        User shortPasswordUser = new User("username3", "correo3@correo.com", "123");
        Assertions.assertThrows(Exception.class, () -> daoMySql.save(shortPasswordUser));

        User longPasswordUser = new User("username4", "correo4@correo.com", "thispasswordiswaytoolong1234567890");
        Assertions.assertThrows(Exception.class, () -> daoMySql.save(longPasswordUser));
    }

    // 2. updateUser test
    @Test
    void testUpdateUser() {
        User usuario = new User(1, "username1", "correo1@correo.com", "newPassword");
        daoMySql.updateUser(usuario);

        try {
            QueryDataSet actualTable = new QueryDataSet(dbConnection);
            actualTable.addTable("updateTMP", "SELECT * FROM usuarios WHERE id = 1");

            assertThat(actualTable.getTable("updateTMP").getValue(0, "username"), is("username1"));
            assertThat(actualTable.getTable("updateTMP").getValue(0, "password"), is("newPassword"));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en el test de actualización: " + e.getMessage());
        }
    }

    // 3. deleteUser test
    @Test
    void testDeleteUser() {
        daoMySql.deleteById(1);

        try {
            QueryDataSet actualTable = new QueryDataSet(dbConnection);
            actualTable.addTable("deleteTMP", "SELECT * FROM usuarios WHERE id = 1");

            Assertions.assertEquals(0, actualTable.getTable("deleteTMP").getRowCount());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en el test de eliminación: " + e.getMessage());
        }
    }

    // 4. findAllUsers test
    @Test
    void testFindAllUsers() {
        Assertions.assertEquals(2, daoMySql.findAll().size());
    }

    // 5. findUserByEmail test
    @Test
    void testFindUserByEmail() {
        User usuario = daoMySql.findUserByEmail("correo1@correo.com");
        assertThat(usuario.getUsername(), is("username1"));
        assertThat(usuario.getEmail(), is("correo1@correo.com"));
    }

    // 6. findUserById test
    @Test
    void testFindUserById() {
        User usuario = daoMySql.findById(1);
        assertThat(usuario.getUsername(), is("username1"));
        assertThat(usuario.getEmail(), is("correo1@correo.com"));
    }
}
