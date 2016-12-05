package com.bmv.notes.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmv.notes.core.Note;
import io.dropwizard.jersey.params.IntParam;

/**
 * Notes Resource
 *
 * @author rboukrim
 */
@Path("/notes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotesResource {

    /**
     * Constructor 
     */
    public NotesResource() {

    }

    /**
     * @return Set<Note> Set of all notes
     */
    @GET
    public Set<Note> getNotes() { 
    	return null;
    }
    
    /**
     * @param noteId id of note to be retrieved
     * @return Note a note by id 
     */
    @GET
    @Path("/{noteId}")
    public Response getNote(@PathParam("noteId") IntParam noteId) {
    	//TODO: implement
    	Note note = new Note();
    	return Response.ok(note).build();
    }
    
    /**
     * @param jsonData note data in json format
     * @return Response
     * @throws URISyntaxException exception on URI instantiation
     */
    @POST
    public Response createNote(String jsonData) throws URISyntaxException {
    	//TODO: implement
    	int newNoteId = 30; //for testing only
    	return Response.created(new URI( String.valueOf(newNoteId) ) ).build();
    } 
    
    /**
     * @param noteId id of note to be updated
     * @param jsonData note data in json format
     * @return Response
     */
    @PUT 
    @Path("/{noteId}")
    public Response updateNote(@PathParam("noteId") IntParam noteId, String jsonData) {   
    	//TODO: implement
    	return Response.ok().build();
    }
    
    /**
     * @param noteId id of note to be deleted
     * @return Response delete a note by id 
     */
    @DELETE 
    @Path("/{noteId}")
    public Response deleteNote(@PathParam("noteId") IntParam noteId) {
    	//TODO: implement
    	return Response.noContent().build();
    }
}
