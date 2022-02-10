package tobyspring.user.dao;

import tobyspring.user.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void add(final User user) throws ClassNotFoundException, SQLException;
    void deleteAll();
    List<User> getAll();
    User get(String id) throws ClassNotFoundException, SQLException;
    int getCount() throws SQLException;
    void update(User user);
}
