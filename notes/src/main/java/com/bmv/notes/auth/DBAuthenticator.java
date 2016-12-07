package com.bmv.notes.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

import com.bmv.notes.core.User;
import com.bmv.notes.db.UserDAO;

/**
 * Class for authenticating users
 */
public class DBAuthenticator implements Authenticator<BasicCredentials, User> {

    /**
     * Reference to UserDAO to check whether the user with the provided credentials
     * exists in the application's database or not
     */
    private final UserDAO userDAO;

    /**
     * Constructor
     *
     * @param userDAO The DAO for the User object necessary to look for users by
     * their credentials.
     * @param jdbi
     * database authentication doesn't work as described in documentation.
     */
    public DBAuthenticator(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Implementation of the authenticate method.
     *
     * @param credentials An instance of the BasicCredentials class containing
     * username and password.
     * @return An Optional containing the user characterized by credentials or
     * an empty optional otherwise.
     * @throws AuthenticationException throws an exception in the case of
     * authentication problems.
     */
    @Override
    public final Optional<User> authenticate(BasicCredentials credentials)
            throws AuthenticationException {
        	Optional<User> result = Optional.ofNullable(userDAO.findByEmailAndPassword(credentials.getUsername(), credentials.getPassword()));
            return result;
    }

}