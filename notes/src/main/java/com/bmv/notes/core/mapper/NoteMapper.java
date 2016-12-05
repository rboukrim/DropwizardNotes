package com.bmv.notes.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		note.setCreateTime(rs.getString("create_time"));
		note.setLastUpdateTime(rs.getString("last_update_time"));
		note.setUserId(rs.getInt("user_id"));
		
		return note;
	}
}
