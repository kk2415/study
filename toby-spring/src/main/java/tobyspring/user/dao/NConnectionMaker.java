package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
        String uid = "TOBY";
        String pwd = "061599";

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection c = DriverManager.getConnection(uri, uid, pwd);
        return c;
    }
}
