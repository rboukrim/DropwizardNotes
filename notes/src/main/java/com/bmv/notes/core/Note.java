package com.bmv.notes.core;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 
 * @author rboukrim
 * 
 */

public class Note {
    
    /*********** private fields ******************/
    private Integer id;
    private String title;
    private String note;
    private String  createTime;
    private String  lastUpdateTime;
    private int userId;

	/****************** Setters and getters ******************/  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    @Length(max=50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Length(max=1000)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String  createTime) {
        this.createTime = createTime;
    }
    
    public String  getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public void setLastUpdateTime(String  lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	//override the toString method
    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", title=" + title
                + ", note=" + note
                + ", create time=" + createTime
                + ", last update time=" + lastUpdateTime
        		+ ", userId=" + userId +'}';
    }
}