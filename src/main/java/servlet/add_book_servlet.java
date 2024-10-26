package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.book_store;
import com.learn.constant.db.book_db;
import com.learn.model.book;
import com.learn.model.user_role;
import com.learn.service.book_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.util.store_util;

public class add_book_servlet extends HttpServlet {
    book_service bookService = new book_service_impl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        // Check if the user is logged in as a seller
        if (!store_util.isLoggedIn(user_role.SELLER, req.getSession())) {
            displayLoginPrompt(req, res, pw);
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("seller_home.html");
        rd.include(req, res);
        store_util.setActiveTab(pw, "addbook");

        // Get the book name, and if it's missing, show the form
        String bName = req.getParameter(book_db.COLUMN_NAME);
        if (bName == null || bName.isBlank()) {
            showAddBookForm(pw);
            return;
        }

        // Process the form submission
        processBookAddition(req, res, pw, bName);
    }

    private void processBookAddition(HttpServletRequest req, HttpServletResponse res, PrintWriter pw, String bName) {
        try {
            // Generate unique book code
            String bCode = UUID.randomUUID().toString();
            String bAuthor = req.getParameter(book_db.COLUMN_AUTHOR);
            double bPrice = Double.parseDouble(req.getParameter(book_db.COLUMN_PRICE));
            int bQty = Integer.parseInt(req.getParameter(book_db.COLUMN_QUANTITY));

            // Create a new book object
            book book = new book(bCode, bName, bAuthor, bPrice, bQty);

            // Call the service to add the book
            String message = bookService.addBook(book);

            if ("SUCCESS".equalsIgnoreCase(message)) {
                displayMessage(pw, "Book Detail Updated Successfully!<br/>Add More Books");
            } else {
                displayMessage(pw, "Failed to Add Books! Please fill up carefully.");
            }

        } catch (NumberFormatException e) {
            displayMessage(pw, "Invalid input! Price and quantity should be numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            displayMessage(pw, "Failed to add books! Please try again.");
        }
    }

    private void displayLoginPrompt(HttpServletRequest req, HttpServletResponse res, PrintWriter pw) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("seller_login.html");
        rd.include(req, res);
        pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
    }

    private void displayMessage(PrintWriter pw, String message) {
        pw.println("<table class=\"tab\"><tr><td>" + message + "</td></tr></table>");
    }

    private static void showAddBookForm(PrintWriter pw) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"addbook\" method=\"post\">\r\n"
                + "                    <label for=\"bookName\">Book Name : </label> <input type=\"text\" name=\"name\" id=\"bookName\" placeholder=\"Enter Book's name\" required><br/>\r\n"
                + "                    <label for=\"bookAuthor\">Book Author : </label><input type=\"text\" name=\"author\" id=\"bookAuthor\" placeholder=\"Enter Author's Name\" required><br/>\r\n"
                + "                    <label for=\"bookPrice\">Book Price : </label><input type=\"number\" name=\"price\" placeholder=\"Enter the Price\" required><br/>\r\n"
                + "                    <label for=\"bookQuantity\">Book Quantity : </label><input type=\"number\" name=\"quantity\" id=\"bookQuantity\" placeholder=\"Enter the quantity\" required><br/>\r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" value=\" Add Book \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "    </table>";
        pw.println(form);
    }
}
