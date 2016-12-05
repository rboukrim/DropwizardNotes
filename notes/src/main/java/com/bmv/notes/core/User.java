package com.bmv.notes.core;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author rboukrim
 */

public class User {
    
    /*********** private fields ******************/ 
    private Integer id;
    private String email;
    private String password;
    private String createTime;
    private String lastUpdateTime;
    private Set<Note> notes;
    
    /****************** Setters and getters ******************/    
    public Integer getId() {
        return id;
    }
    
	public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    @Email
    @Length(max=100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @Length(min=8, max=64)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public void setLastUpdateTime(String lastUpdateTime) {    	
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public Set<Note> getNotes() {
		return notes;
	}
    
	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}  

	//override the toString method
    @Override
    public String toString() {
        return "User{" + "id=" + id 
        		+ ", email=" + email
                + ", password=" + password
                + ", createTime=" + createTime
                + ", lastUpdateTime=" + lastUpdateTime
                + ", notes=" + notes
                + '}';
    }
}