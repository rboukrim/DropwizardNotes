package com.bmv.notes;

import org.skife.jdbi.v2.DBI;

import com.bmv.notes.resources.NotesResource;

import io.dropwizard.Application;
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
        
        // Add the notes resource to the environment
        environment.jersey().register(new NotesResource(jdbi));
    }

}
