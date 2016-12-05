package com.bmv.notes.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.bmv.notes.core.User;
import com.bmv.notes.core.mapper.UserMapper;
import java.util.Optional;

/**
 * User Data Access Object.
 *
 * @author rboukrim
 */

@RegisterMapper(UserMapper.class)
public interface UserDAO {
	
	@SqlQuery("SELECT * from users where id = :id")
	Optional<User> findById(@Bind("id") int id);
	
	@SqlQuery("SELECT * from users where email = :email")
	User findByEmail(@Bind("email") String email);
	
	@SqlQuery("SELECT * from users where email = :email and password = :password")
	User findByEmailAndPassword(@Bind("email") String email, @Bind("password") String password); 
}