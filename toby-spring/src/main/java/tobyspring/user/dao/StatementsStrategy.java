package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementsStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
