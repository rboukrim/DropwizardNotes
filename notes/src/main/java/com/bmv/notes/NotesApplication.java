package com.bmv.notes;

import com.bmv.notes.resources.NotesResource;

import io.dropwizard.Application;
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
        
        // Add the notes resource to the environment
        environment.jersey().register(new NotesResource());
    }

}
