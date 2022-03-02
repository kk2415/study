package spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MemberDao {

	private static long nextId = 0;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Member selectByEmail(String email) {
		List<Member> list = jdbcTemplate.query("SELECT * FROM member WHERE email = ?", new RowMapper<Member>() {

			@Override
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = null;
				
				if (rs.next()) {
					member = new Member(rs.getString("email"), rs.getString("password"),
							rs.getString("name"), rs.getTimestamp("regdate").toLocalDateTime());
					member.setId(rs.getLong("id"));
				}
				return member;
			}
			
		}, email);
		return list.isEmpty() ? null : list.get(0);
	}

//	public void insert(Member member) {
//	}
//
//	public void update(Member member) {
//	}
//
//	public Collection<Member> selectAll() {
//	}
}
