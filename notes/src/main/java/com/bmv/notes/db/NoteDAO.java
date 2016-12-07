package com.bmv.notes.db;

import java.util.Set;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.bmv.notes.core.Note;
import com.bmv.notes.core.mapper.NoteMapper;

/**
 * Note Data Access Object
 *
 * @author rboukrim
 */
public interface NoteDAO {
	
	@RegisterMapper(NoteMapper.class)
	@SqlQuery("SELECT * FROM notes WHERE user_id = :user_id")
	Set<Note> findByUserId(@Bind("user_id") int userId);
	
	@RegisterMapper(NoteMapper.class)
	@SqlQuery("SELECT * FROM notes WHERE id = :id")
	Note findById(@Bind("id") int id);
	
	@RegisterMapper(NoteMapper.class)
	@SqlQuery("SELECT * FROM notes WHERE id = :id AND user_id = :user_id")
	Note findByIdAndUserId(@Bind("id") int id, @Bind("user_id") int user_id);
	
	@SqlUpdate("INSERT INTO notes (title, note, user_id) VALUES (:s.title, :s.note, :user_id)")
	@GetGeneratedKeys
	int create(@BindBean("s") Note note, @Bind("user_id") int user_id);
	
	@SqlUpdate("UPDATE notes SET title = :title, note = :note WHERE id = :id")
	int save(@BindBean Note note);
	
	@SqlUpdate("DELETE FROM notes WHERE id = :id")
	int delete(@Bind("id") int id);
}