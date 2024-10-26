package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.response_code;
import com.learn.model.user_role;
import com.learn.service.book_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.util.store_util;

public class remove_book_servlet extends HttpServlet {

    book_service bookService = new book_service_impl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        if (!store_util.isLoggedIn(user_role.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("seller_login.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        try {
            String bookId = req.getParameter("bookId");
            RequestDispatcher rd = req.getRequestDispatcher("seller_home.html");
            rd.include(req, res);
            store_util.setActiveTab(pw, "removebook");
            pw.println("<div class='container'>");
            if (bookId == null || bookId.isBlank()) {
                // render the remove book form;
                showRemoveBookForm(pw);
                return;
            } // else continue

            String responseCode = bookService.deleteBookById(bookId.trim());
            if (response_code.SUCCESS.name().equalsIgnoreCase(responseCode)) {
                pw.println("<table class=\"tab my-5\"><tr><td>Book Removed Successfully</td></tr></table>");
                pw.println(
                        "<table class=\"tab\"><tr><td><a href=\"removebook\">Remove more Books</a></td></tr></table>");

            } else {
                pw.println("<table class=\"tab my-5\"><tr><td>Book Not Available In The Store</td></tr></table>");
                pw.println(
                        "<table class=\"tab\"><tr><td><a href=\"removebook\">Remove more Books</a></td></tr></table>");
            }
            pw.println("</div>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Remove Books! Try Again</td></tr></table>");
        }
    }

    private static void showRemoveBookForm(PrintWriter pw) {
        String form = "<form action=\"removebook\" method=\"post\" class='my-5'>\r\n"
                + "        <table class=\"tab\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <label for=\"bookCode\">Enter BookId to Remove </label>\r\n"
                + "                <input type=\"text\" name=\"bookId\" placeholder=\"Enter Book Id\" id=\"bookCode\" required>\r\n"
                + "                <input class=\"btn btn-danger my-2\" type=\"submit\" value=\"Remove Book\">\r\n"
                + "            </td>\r\n"
                + "        </tr>\r\n"
                + "\r\n"
                + "        </table>\r\n"
                + "    </form>";
        pw.println(form);
    }

}

