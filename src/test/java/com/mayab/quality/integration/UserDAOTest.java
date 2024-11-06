package com.mayab.quality.integration;

import java.io.FileInputStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

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
        // Initialize DAO
        daoMySql = new UserMysqlDAO();
        // Set the initial condition of the database
        IDatabaseConnection connection = getConnection();
        try {
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, getDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        } catch (Exception e) {
            Assertions.fail("Error in setup: " + e.getMessage());
        } finally {
            connection.close();
        }
    }

    private IDatabaseConnection getConnection() throws Exception {
        // Provide logic here to establish and return a DBUnit connection
        return new PropertiesBasedJdbcDatabaseTester().getConnection();
    }

    private IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @Test
    void testAddUserQry() {
        // init
        User usuario = new User("username2", "correo2@correo.com", "123456");
        // exercise
        int newID = daoMySql.save(usuario);
        daoMySql.save(usuario);

        // Verify data in database
        try {
            IDatabaseConnection conn = getConnection(); // connection
            QueryDataSet actualTable = new QueryDataSet(conn);
            actualTable.addTable("insertTMP", "SELECT * FROM usuarios WHERE id = " + newID);

            String actualName = (String) actualTable.getTable("insertTMP").getValue(0, "name");
            String actualEmail = (String) actualTable.getTable("insertTMP").getValue(0, "email");
            String actualPassword = (String) actualTable.getTable("insertTMP").getValue(0, "password");

            assertThat(actualName, is(usuario.getName()));
            assertThat(actualEmail, is(usuario.getEmail()));
            assertThat(actualPassword, is(usuario.getPassword()));
        } catch (Exception e) {
            Assertions.fail("Error in insert test: " + e.getMessage());
        }
    }
}
