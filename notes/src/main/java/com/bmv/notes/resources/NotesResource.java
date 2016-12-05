package com.bmv.notes.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;

import com.bmv.notes.db.NoteDAO;
import com.bmv.notes.core.Note;
import com.bmv.notes.core.User;

import io.dropwizard.auth.Auth;
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
     * Error messages - COnstancts
     */
    private static final String NOTE_NOT_FOUND = "Note not found";
    private static final String FORBIDDEN_ACCESS = "Access denied to this note";

    /**
     * DAO to manipulate notes.
     */
    private final NoteDAO noteDAO;
    
    /**
     * Constructor to initialize DAO.
     *
     * @param noteDAO DAO to manipulate notes.
     */
    public NotesResource(final DBI jdbi) {
        this.noteDAO = jdbi.onDemand(NoteDAO.class);
    }

    /**
     * @return Set<Note> Set of all notes
     */
    @GET
    public Set<Note> getNotes(@Auth User user) { 
    	Set<Note> notes = this.noteDAO.findByUserId(user.getId());
    	user.setNotes(notes);
    	return notes;
    }
    
    /**
     * @param noteId id of note to be retrieved
     * @return Note a note by id 
     */
    @GET
    @Path("/{noteId}")
    public Response getNote(@PathParam("noteId") IntParam noteId, @Auth User user) {
    	Note note = getNoteOrThrowException(noteId, user);
    	return Response.ok(note).build();
    }
    
    /**
     * @param jsonData note data in json format
     * @return Response
     * @throws URISyntaxException exception on URI instantiation
     */
    @POST
    public Response createNote(String jsonData, @Auth User user) throws URISyntaxException {
    	//TODO: implement Basic Authentication to get user and user id
    	//TODO: map jsonData to Note object and insert the note in DB
    	int newNoteId = 30;
    	return Response.created( new URI( String.valueOf(newNoteId) ) ).build();
    } 
    
    /**
     * @param noteId id of note to be updated
     * @param jsonData note data in json format
     * @return Response
     */
    @PUT 
    @Path("/{noteId}")
    public Response updateNote(@PathParam("noteId") IntParam noteId, String jsonData, @Auth User user) { 
    	Note note = getNoteOrThrowException(noteId, user);
    	//TODO: map jsonData to Note object and insert the note in DB
    	//this.noteDAO.save(new Note());
    	return Response.ok().build();
    }
    
    /**
     * @param noteId id of note to be deleted
     * @return Response delete a note by id 
     */
    @DELETE 
    @Path("/{noteId}")
    public Response deleteNote(@PathParam("noteId") IntParam noteId, @Auth User user) {
    	getNoteOrThrowException(noteId, user);
    	noteDAO.delete(noteId.get());
    	return Response.noContent().build();
    }
    
    
    /**
     * Method gets note by id and checks if the authenticated user is the owner of the note.
     * It returns the note or throws 403 ForbiddenException otherwise.
     *
     * @param id the id of a note.
     * @param user the id of the user.
     * @return note
     */
    private Note getNoteOrThrowException(IntParam id, @Auth User user) {        
    	Optional<Note> note = Optional.ofNullable( this.noteDAO.findById( id.get()) );
    	if ( !note.isPresent() ) {
    		throw new WebApplicationException(NOTE_NOT_FOUND, new NotFoundException(), Response.Status.NOT_FOUND);
    	} else if ( note.get().getUserId() != user.getId() ) {
            throw new WebApplicationException(FORBIDDEN_ACCESS, new ForbiddenException(), Response.Status.FORBIDDEN);
    	}
       
        return note.get();
    }
}
