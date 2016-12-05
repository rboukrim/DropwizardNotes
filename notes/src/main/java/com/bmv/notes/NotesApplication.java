package com.bmv.notes;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import com.bmv.notes.auth.DBAuthenticator;
import com.bmv.notes.core.User;
import com.bmv.notes.resources.NotesResource;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class NotesApplication extends Application<NotesConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NotesApplication().run(args);
    }

    @Override
    public String getName() {
        return "Notes";
    }

    @Override
    public void initialize(final Bootstrap<NotesConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final NotesConfiguration configuration,
                    final Environment environment) {
    	
    	// Create a DBI factory and build a jdbi instance
    	final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        
        // Register DB authenticator to check user's credentials against users table in the database
        DBAuthenticator authenticator = new DBAuthenticator(jdbi);
        environment.jersey().register(
        		new AuthDynamicFeature(
	                new BasicCredentialAuthFilter.Builder<User>()
	                .setAuthenticator(authenticator)
	                .setAuthorizer(new Authorizer<User>() {
	                    @Override
	                    public boolean authorize(User principal, String role) {
	                        return true;
	                    }
	                })
	                .setRealm("Web Service Realm")
	                .buildAuthFilter()
        		)
        	);
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        
        //Necessary if @Auth is used to inject a custom Principal type into resource
        environment.jersey().register( new AuthValueFactoryProvider.Binder<>(User.class) );
        
        // Add the notes resource to the environment
        environment.jersey().register(new NotesResource(jdbi));
    }

}
