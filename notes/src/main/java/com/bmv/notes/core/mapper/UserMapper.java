package com.bmv.notes.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		dateFormat.setTimeZone(tz);

		String createTimeString = dateFormat.format(rs.getTimestamp("create_time"));
		user.setCreateTime(createTimeString);
		
		String lastUpdateTimestamp = dateFormat.format(rs.getTimestamp("last_update_time"));
		user.setLastUpdateTime(lastUpdateTimestamp);
		
		return user;
	}
}
