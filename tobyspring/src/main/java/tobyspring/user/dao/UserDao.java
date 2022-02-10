package tobyspring.user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import tobyspring.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws ClassNotFoundException, SQLException {
        /*이런 편리한 기능이...*/
        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        /* 콜백/템플릿 패턴을 이용한 jdbctemplate */
        this.jdbcTemplate.update("delete from users"); /* 내장 콜백을 이용했기 때문에 그냥 쿼리문만 건네주면 된다. */
    }

    public List<User> getAll() {
        return (List<User>) this.jdbcTemplate.queryForObject("select * from users order by id", this.userMapper);
        /* query 템플릿은 SQL을 실행해서 얻은 ResultSet의 모든 로우를 열람하면서 로우마다 RowMapper 콜백을 호출한다. */
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id},
                this.userMapper
        );
        /* ResultSet객체에서 어떤 값을 받아오고 싶다? 그럴 땐 RowMapper 인터페이스를 구현하자
         * RowMapper가 호출되는 시점에서 RowMapper은 첫 번째 로우를 가리키고 있다. 그러므로
         * rs.next()를 호출할 필요는 없다.
         * 또한 RowMapper은 sql을 실행해서 받은 로우의 개수가 하나가 아니라면 예외롤 던지도록 만들어져 있다.
         * 이때 던지는 에러가 EmptyResultDataAccessException이다.
         * */
    }

    public int getCount() throws SQLException {
//        return this.jdbcTemplate.query(
//                new PreparedStatementCreator() {
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                        return con.prepareStatement("select count(*) from users");
//                    }
//                }, new ResultSetExtractor<Integer>() {
//                    @Override
//                    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                        rs.next();
//                        return rs.getInt(1);
//                    }
//                }
//        );

        /* queryForInt()는 Deprecated 됨... */
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

    }
}
