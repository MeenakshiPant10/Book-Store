package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.model.user_role;
import com.learn.util.store_util;

public class about_servlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        String homePage = null;
        user_role role = null;

        if (store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            homePage = "customer_home.html";
            role = user_role.CUSTOMER;
        } else if (store_util.isLoggedIn(user_role.SELLER, req.getSession())) {
            homePage = "seller_home.html";
            role = user_role.SELLER;
        }

        if (homePage != null) {
            RequestDispatcher rd = req.getRequestDispatcher(homePage);
            rd.include(req, res);
            store_util.setActiveTab(pw, "about");
            pw.println("<iframe src=\"https://example.com\" class=\"holds-the-iframe\" title=\"About Us\" width=\"100%\" height=\"100%\"></iframe>");
        } else {
            RequestDispatcher rd = req.getRequestDispatcher("log_in.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
        }
    }
}
