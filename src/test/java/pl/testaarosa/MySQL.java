package pl.testaarosa;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;

public class MySQL {
    @Test
    public void testMySQLConnection() throws SQLException {

        Connection conn = null;
        try {
            String username = "testaarosa";
            String password = "Sd7udnmDnU_?";
            String dbUrl = "jdbc:mysql://mysql4.gear.host/testaarosa";
            conn = DriverManager.getConnection(dbUrl, username, password);
            assertFalse(conn.isClosed());
            //      System.out.println("Connection Established to MYSQL Database");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            assert conn != null;
            conn.close();
        }
    }
}

