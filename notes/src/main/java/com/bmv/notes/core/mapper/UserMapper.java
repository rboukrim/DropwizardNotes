package com.bmv.notes.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.bmv.notes.core.User;
/**
 * User ResultSet mapper
 *
 * @author rboukrim
 */
public class UserMapper implements ResultSetMapper<User>{
	@Override
	public User map(int idx, ResultSet rs, StatementContext ctx) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setEmail( rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setCreateTime(rs.getString("create_time"));
		user.setLastUpdateTime(rs.getString("last_update_time"));
		
		return user;
	}
}
