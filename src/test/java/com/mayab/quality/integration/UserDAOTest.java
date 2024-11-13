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

    @Test
    void testAddUserQry() {
        String plainPassword = "123456";
        User usuario = new User("username2", "correo2@correo.com", plainPassword);

        int newID = daoMySql.save(usuario);

        try {
            QueryDataSet actualTable = new QueryDataSet(dbConnection);
            actualTable.addTable("insertTMP", "SELECT * FROM usuarios WHERE id = " + newID);

            String actualUsername = (String) actualTable.getTable("insertTMP").getValue(0, "username");
            String actualEmail = (String) actualTable.getTable("insertTMP").getValue(0, "email");
            String actualPassword = (String) actualTable.getTable("insertTMP").getValue(0, "password");

            assertThat(actualUsername, is(usuario.getName()));
            assertThat(actualEmail, is(usuario.getEmail()));
            assertThat(actualPassword, is(plainPassword));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Error en el test de inserción: " + e.getMessage());
        }
    }
}
