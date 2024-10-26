package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.book_store;
import com.learn.model.book;
import com.learn.model.user_role;
import com.learn.service.book_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.util.store_util;

public class buy_book_servlet extends HttpServlet {
    book_service bookService = new book_service_impl();

    // Handles both GET and POST requests
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        // Check if the user is logged in as a customer
        if (!store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            displayLoginPrompt(req, res, pw);
            return;
        }

        // Fetch available books and display them in a form
        try {
            List<book> books = bookService.getAllBooks();
            if (books == null || books.isEmpty()) {
                throw new Exception("Book list is null or empty");
            }
            RequestDispatcher rd = req.getRequestDispatcher("customer_home.html");
            rd.include(req, res);
            store_util.setActiveTab(pw, "cart");

            pw.println("<div class=\"tab hd brown \">Books Available In Our Store</div>");
            pw.println("<div class=\"tab\"><form action=\"buys\" method=\"post\">");
            pw.println("<table>\r\n" +
                    "			<tr>\r\n" +
                    "				<th>Select</th>\r\n" +
                    "				<th>Code</th>\r\n" +
                    "				<th>Name</th>\r\n" +
                    "				<th>Author</th>\r\n" +
                    "				<th>Price</th>\r\n" +
                    "				<th>Available</th>\r\n" +
                    "				<th>Quantity</th>\r\n" +
                    "			</tr>");

            int index = 0;
            for (book book : books) {
                index++;
                String checkboxName = "checked" + index;
                String quantityName = "qty" + index;

                pw.println("<tr>");
                pw.println("<td><input type=\"checkbox\" name=\"" + checkboxName + "\" value=\"" + book.getBarcode() + "\"></td>");
                pw.println("<td>" + book.getBarcode() + "</td>");
                pw.println("<td>" + book.getName() + "</td>");
                pw.println("<td>" + book.getAuthor() + "</td>");
                pw.println("<td>" + book.getPrice() + "</td>");
                pw.println("<td>" + book.getQuantity() + "</td>");
                pw.println("<td><input type=\"number\" name=\"" + quantityName + "\" value=\"0\" min=\"0\" max=\"" + book.getQuantity() + "\" required></td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("<input type=\"submit\" value=\"PAY NOW\">");
            pw.println("</form></div>");

        } catch (NumberFormatException e) {
            pw.println("<div>Error parsing number. Please make sure inputs are correct.</div>");
            e.printStackTrace();
        } catch (Exception e) {
            pw.println("<div>Internal error occurred: " + e.getMessage() + "</div>");
            e.printStackTrace();
        }
        }


    private void displayLoginPrompt(HttpServletRequest req, HttpServletResponse res, PrintWriter pw) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
        rd.include(req, res);
        pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
    }
}
