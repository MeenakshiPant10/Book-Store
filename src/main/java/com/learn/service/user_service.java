
package com.learn.service;

import javax.servlet.http.HttpSession;

import com.learn.model.store_exception;
import com.learn.model.user;
import com.learn.model.user_role;

public interface user_service {

    public user login(user_role role, String email, String password, HttpSession session) throws store_exception;

    public String register(user_role role, user user) throws store_exception;

    public boolean isLoggedIn(user_role role, HttpSession session);
    public boolean logout(HttpSession session);
    
}
