
package com.learn.service_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.learn.constant.response_code;
import com.learn.constant.db.user_db;
import com.learn.model.store_exception;
import com.learn.model.user;
import com.learn.model.user_role;
import com.learn.service.user_service;
import com.learn.util.db_util;

public class user_service_impl implements user_service {

    private static final String registerUserQuery = "INSERT INTO " + user_db.TABLE_USERS
            + "  VALUES(?,?,?,?,?,?,?,?)";

    private static final String loginUserQuery = "SELECT * FROM " + user_db.TABLE_USERS + " WHERE "
            + user_db.COLUMN_USERNAME + "=? AND " + user_db.COLUMN_PASSWORD + "=? AND "
            + user_db.COLUMN_USERTYPE + "=?";

    @Override
    public user login(user_role role, String email, String password, HttpSession session) throws store_exception{
        Connection con = db_util.getConnection();
        PreparedStatement ps;
        user user = null;
        try {
            String userType = user_role.SELLER.equals(role) ? "1" : "2";
            ps = con.prepareStatement(loginUserQuery);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, userType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new user();
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setPhone(rs.getLong("phone"));
                user.setEmailId(email);
                user.setPassword(password);
                session.setAttribute(role.toString(), user.getEmailId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean isLoggedIn(user_role role, HttpSession session) {
        if (role == null)
            role = user_role.CUSTOMER;
        return session.getAttribute(role.toString()) != null;
    }

    @Override
    public boolean logout(HttpSession session) {
        session.removeAttribute(user_role.CUSTOMER.toString());
        session.removeAttribute(user_role.SELLER.toString());
        session.invalidate();
        return true;
    }

    @Override
    public String register(user_role role, user user) throws store_exception {
        String responseMessage = response_code.FAILURE.name();
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(registerUserQuery);
            ps.setString(1, user.getEmailId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getAddress());
            ps.setLong(6, user.getPhone());
            ps.setString(7, user.getEmailId());
            int userType = user_role.SELLER.equals(role) ? 1 : 2;
            ps.setInt(8, userType);
            int k = ps.executeUpdate();
            if (k == 1) {
                responseMessage = response_code.SUCCESS.name();
                ;
            }
        } catch (Exception e) {
            responseMessage += " : " + e.getMessage();
            if (responseMessage.contains("Duplicate"))
                responseMessage = "User already registered with this email !!";
            e.printStackTrace();
        }
        return responseMessage;
    }

}

