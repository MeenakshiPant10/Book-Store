package com.learn.service;

import javax.servlet.http.HttpSession;

import com.learn.model.contact;
import com.learn.model.store_exception;

public interface contact_service {

	 public String AddContact(contact cont ) throws store_exception;
	    
}
