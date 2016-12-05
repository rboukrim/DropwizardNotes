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
	@SqlUpdate("CREATE TABLE IF NOT EXISTS `notes` ("
			+ " `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,"
			+ " `title` varchar(50) NOT NULL,"
			+ " `note` varchar(1000) DEFAULT NULL,"
			+ " `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ " `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
			+ " `user_id` int(11) NOT NULL"
			+ " PRIMARY KEY (`id`),"
			+ " FOREIGN KEY(`user_id`) REFERENCES users(`id`) )"
		)
	void createNotesTable();
	
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
	
	@SqlUpdate("UPDATE notes SET title = :title, note = :note WHERE id")
	int save(@BindBean Note note);
	
	@SqlUpdate("DELETE FROM notes WHERE id = :id")
	int delete(@Bind("id") int id);
}