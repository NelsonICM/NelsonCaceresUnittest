package com.mayab.quality.integration;

import java.io.File;
import java.io.FileInputStream;

import org.dbunit.Assertion;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

class UserDAOTest {

    private UserMysqlDAO daoMySql;
    private IDatabaseConnection connection;

    public UserDAOTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad2024");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456");
    }

    @BeforeEach
    void setUp() throws Exception {
        daoMySql = new UserMysqlDAO();
        connection = getConnection();
        try {
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, getDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        } catch (Exception e) {
            throw new RuntimeException("Error in setup: " + e.getMessage(), e);
        }
    }

    private IDatabaseConnection getConnection() throws Exception {
        DataSource dataSource = createDataSource();
        return new DatabaseDataSourceConnection(dataSource);
    }

    private DataSource createDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3307/calidad2024");
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    private IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @Test
    void testAgregarUsuario() {
        User usuario = new User("username2", "correo2@correo.com", "123456");
        daoMySql.save(usuario);

        try {
            connection.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            ITable actualTable = connection.createDataSet().getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/create.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);
        } catch (Exception e) {
            throw new RuntimeException("Error in insert test: " + e.getMessage(), e);
        }
    }
}
