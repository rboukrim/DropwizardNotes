package com.bmv.notes.resources;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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

import org.apache.commons.beanutils.BeanUtils;
import org.skife.jdbi.v2.DBI;

import com.bmv.notes.db.NoteDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String WRONG_DATA_FORMAT = "Wrong data format";

    /**
     * DAO to manipulate notes.
     */
    private final NoteDAO noteDAO;
    
    /**
     * Hibernate Validator to validate note data
     */
    private final Validator validator;

    
    /**
     * Constructor to initialize DAO.
     *
     * @param noteDAO DAO to manipulate notes.
     */
    public NotesResource(final NoteDAO noteDAO, final  Validator validator) {
        this.noteDAO = noteDAO;
        this.validator = validator;
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
    	Note note = new Note();
    	
    	// Update note data
    	note = mapJsonToNoteObject(jsonData, note);
        
        // Validate the note's data
        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        
        // Are there any constraint violations?
        if (violations.size() > 0) {
        	// Validation errors occurred. get formatted validation messages
        	ArrayList<String> validationMessages = getValidationMessages(violations);
        	// Return Response with validation messages
        	return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        } else {
        	// OK, no validation errors
        	// Store the new note
        	int newNoteId = this.noteDAO.create(note, user.getId());
        	return Response.created( new URI( String.valueOf(newNoteId) ) ).build();
        }
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
    	
    	// Update note data
    	note = mapJsonToNoteObject(jsonData, note);
     
        // Validate the note's data
        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        // Are there any constraint violations?
        if (violations.size() > 0) {
        	// Validation errors occurred. get formatted validation messages
        	ArrayList<String> validationMessages = getValidationMessages(violations);
        	// Return Response with validation messages
        	return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        } else {
        	// OK, no validation errors
        	// Save note
        	noteDAO.save(note);
            return Response.ok().build();
        } 
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
    
    /**
     * Method updates note object with data in JSON string
     * throws 403 ForbiddenException  otherwise.
     *
     * @param jsonData json string
     * @param note note object
     * @return note
     */
	private Note mapJsonToNoteObject(String jsonData, Note note) {
        Map<String, String> changeMap = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
			changeMap = objectMapper.readValue(jsonData, HashMap.class);
			purgeMap(changeMap);
			BeanUtils.populate(note, changeMap);
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            		throw new WebApplicationException(WRONG_DATA_FORMAT, e, Response.Status.BAD_REQUEST);
    	} finally {
            if (changeMap != null) {
                changeMap.clear();
            }
    	}
		return note;
	}
	
    /**
     * A method to remove null values (for unset fields) from the change map. Necessary
     * if not all fields in the changed object are filled.
     *
     * @param changeMap map of object field values.
     */
    private void purgeMap(final Map<String, String> changeMap) {
        changeMap.remove("id");
        changeMap.entrySet().removeIf(  entry -> entry.getValue().equals(null) );
    }
    
    /**
     * A method to return a list of formatted validation messages.
     *
     * @param violations 
     * @return ArrayList of validation messages
     */
    private ArrayList<String> getValidationMessages(Set<ConstraintViolation<Note>> violations) {
    	// Validation errors occurred
    	ArrayList<String> validationMessages = new ArrayList<String>();
    	for (ConstraintViolation<Note> violation : violations) {
    		validationMessages.add(violation.getPropertyPath().toString() + ":" + violation.getMessage());
    	}
    	return validationMessages;
    }
}
