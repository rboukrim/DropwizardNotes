package com.bmv.notes.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.bmv.notes.core.Note;
/**
 * Note ResultSet mapper
 *
 * @author rboukrim
 */
public class NoteMapper implements ResultSetMapper<Note> {

	@Override
	public Note map(int idx, ResultSet rs, StatementContext ctx) throws SQLException {
		Note note = new Note();
		note.setId(rs.getInt("id"));
		note.setTitle(rs.getString("title"));
		note.setNote( rs.getString("note"));
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		dateFormat.setTimeZone(tz);

		String createTimeString = dateFormat.format(rs.getTimestamp("create_time"));
		note.setCreateTime(createTimeString);
		
		String lastUpdateTimestamp = dateFormat.format(rs.getTimestamp("last_update_time"));
		note.setLastUpdateTime(lastUpdateTimestamp);
		note.setUserId(rs.getInt("user_id"));
		
		return note;
	}
}
