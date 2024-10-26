package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.book_store;
import com.learn.constant.db.user_db;
import com.learn.model.user;
import com.learn.model.user_role;
import com.learn.service.user_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.service_implementation.user_service_impl;

public class seller_login_servlet extends HttpServlet {

    user_service userService = new user_service_impl();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);
        String uName = req.getParameter(user_db.COLUMN_USERNAME);
        String pWord = req.getParameter(user_db.COLUMN_PASSWORD);
        try {
            user user = userService.login(user_role.SELLER, uName, pWord, req.getSession());
            if (user != null) {
                RequestDispatcher rd = req.getRequestDispatcher("seller_home.html");

                rd.include(req, res);
                pw.println("    <div id=\"topmid\"><h1>Welcome to Online <br>Book Store</h1></div>\r\n"
                        + "    <br>\r\n"
                        + "    <table class=\"tab\">\r\n"
                        + "        <tr>\r\n"
                        + "            <td><p>Welcome "+user.getFirstName()+", Happy Learning !!</p></td>\r\n"
                        + "        </tr>\r\n"
                        + "    </table>");
            } else {

                RequestDispatcher rd = req.getRequestDispatcher("seller_login.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Incorrect UserName or PassWord</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
