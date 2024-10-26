package com.learn.service_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.learn.constant.response_code;
import com.learn.constant.db.add_contact_db;
import com.learn.model.store_exception;
import com.learn.model.contact;
import com.learn.util.db_util;
import com.learn.service.*;


public class contact_service_impl implements contact_service
{
	private static final String submitQuery = "INSERT INTO " + add_contact_db.TABLE_CONTACT
            + "  VALUES(?,?,?,?,?)";
	
     @Override
	 public String AddContact(contact cont) throws store_exception {
	        String responseMessage = response_code.FAILURE.name();
	        Connection con = db_util.getConnection();
	        try {
	            PreparedStatement ps = con.prepareStatement(submitQuery);
	            ps.setString(1, cont.getEmailId());
	            ps.setString(2, cont.getFirstName());
	            ps.setString(3, cont.getLastName());
	            ps.setNString(4, cont.getSubject());
	            ps.setLong(5, cont.getphone());
	            int k = ps.executeUpdate();
	            if (k == 1) {
	                responseMessage = response_code.SUCCESS.name();
	                ;
	            }
	        } catch (Exception e) {
	           
	            e.printStackTrace();
	        }
	        return responseMessage;
}
}
