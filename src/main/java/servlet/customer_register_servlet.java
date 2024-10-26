package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.book_store;
import com.learn.constant.response_code;
import com.learn.constant.db.user_db;
import com.learn.model.user;
import com.learn.model.user_role;
import com.learn.service.user_service;
import com.learn.service_implementation.user_service_impl;

public class customer_register_servlet extends HttpServlet {

    user_service userService = new user_service_impl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        String pWord = req.getParameter(user_db.COLUMN_PASSWORD);
        String fName = req.getParameter(user_db.COLUMN_FIRSTNAME);
        String lName = req.getParameter(user_db.COLUMN_LASTNAME);
        String addr = req.getParameter(user_db.COLUMN_ADDRESS);
        String phNo = req.getParameter(user_db.COLUMN_PHONE);
        String mailId = req.getParameter(user_db.COLUMN_MAILID);
        user user = new user();
        user.setEmailId(mailId);
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setPassword(pWord);
        user.setPhone(Long.parseLong(phNo));
        user.setAddress(addr);
        try {
            String respCode = userService.register(user_role.CUSTOMER, user);
            System.out.println(respCode);
            if (response_code.SUCCESS.name().equalsIgnoreCase(respCode)) {
                RequestDispatcher rd = req.getRequestDispatcher("customer_login.html");
                rd.include(req, res);
                pw.println("<table class=\"tab\"><tr><td>User Registered Successfully</td></tr></table>");
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("customer_register.html");
                rd.include(req, res);
                pw.println("<table class=\"tab\"><tr><td>" + respCode + "</td></tr></table>");
                pw.println("Sorry for interruption! Try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            }
}
