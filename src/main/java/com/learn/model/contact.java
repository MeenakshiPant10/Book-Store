package com.learn.model;

import java.io.Serializable;
import java.util.List;

public class contact implements Serializable {

	private String emailId;
    
    private String firstName;
    private String lastName;
    private String Subject;
    private long phone;
    public contact(String firstName, String lastName, String Subject, long phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Subject = Subject;
        this.phone=phone;;
        
    }
    public contact() {
        super();
    }
    

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

   

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }

    public long getphone() {
        return phone;
    }

    public void setphone(long phone) {
        this.phone = phone;
    }

  
}
