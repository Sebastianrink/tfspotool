import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

/**
 * Created by sebmaster on 18.03.18.
 */

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() throws SQLException, ClassNotFoundException {

        //Arrange
        Connection conn = getConnection();

        //Act
        final Properties clientInfo = conn.getClientInfo();

        //Assert
        Assert.assertNotNull(clientInfo);
    }

    @Test
    public void testQuery() throws SQLException, ClassNotFoundException {

        //Arrange
        Connection conn = getConnection();

        //Act
        final PreparedStatement statement = conn.prepareStatement("select * from testtabelle");
        ResultSet resultSet = statement.executeQuery();

        //Assert
        Assert.assertNotNull(resultSet);
    }
    @Test
    public void testAddEntry() throws SQLException, ClassNotFoundException {

        //Arrange
        Connection conn = getConnection();

        //Act
        final PreparedStatement statement = conn.prepareStatement("Insert into testtabelle DEFAULT VALUES");
        int i = statement.executeUpdate();

        //Assert
        Assert.assertTrue(i > 0);
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/sebstestdb";
        Properties props = new Properties();
        props.setProperty("user","testuser");
        props.setProperty("password","1qay!QAY");
        //props.setProperty("ssl","true");
        return DriverManager.getConnection(url, props);
    }
}
