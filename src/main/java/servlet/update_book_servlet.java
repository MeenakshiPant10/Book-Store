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
import com.learn.constant.db.book_db;
import com.learn.model.book;
import com.learn.model.user_role;
import com.learn.service.book_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.util.store_util;

public class update_book_servlet extends HttpServlet {
    book_service bookService = new book_service_impl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        if (!store_util.isLoggedIn(user_role.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("seller_login.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("seller_home.html");
        rd.include(req, res);
        store_util.setActiveTab(pw, "storebooks");
        pw.println("<div class='container my-2'>");

        try {
            if (req.getParameter("updateFormSubmitted") != null) {
                String bName = req.getParameter(book_db.COLUMN_NAME);
                String bCode = req.getParameter(book_db.COLUMN_BARCODE);
                String bAuthor = req.getParameter(book_db.COLUMN_AUTHOR);
                double bPrice = Double.parseDouble(req.getParameter(book_db.COLUMN_PRICE));
                int bQty = Integer.parseInt(req.getParameter(book_db.COLUMN_QUANTITY));

                book book = new book(bCode, bName, bAuthor, bPrice, bQty);
                String message = bookService.updateBook(book);
                if (response_code.SUCCESS.name().equalsIgnoreCase(message)) {
                    pw.println(
                            "<table class=\"tab\"><tr><td>Book Detail Updated Successfully!</td></tr></table>");
                } else {
                    pw.println("<table class=\"tab\"><tr><td>Failed to Update Book!!</td></tr></table>");
                    // rd.include(req, res);
                }

                return;
            }

            String bookId = req.getParameter("bookId");

            if (bookId != null) {
                book book = bookService.getBookById(bookId);
                showUpdateBookForm(pw, book);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Load Book data!!</td></tr></table>");
        }
    }

    private static void showUpdateBookForm(PrintWriter pw, book book) {
        String form = "<table class=\"tab my-5\" style=\"width:40%;\">\r\n"
                + "        <tr>\r\n"
                + "            <td>\r\n"
                + "                <form action=\"updatebook\" method=\"post\">\r\n"
                + "                    <label for=\"bookCode\">Book Code : </label><input type=\"text\" name=\"barcode\" id=\"bookCode\" placeholder=\"Enter Book Code\" value='"
                + book.getBarcode() + "' readonly><br/>"
                + "                    <label for=\"bookName\">Book Name : </label> <input type=\"text\" name=\"name\" id=\"bookName\" placeholder=\"Enter Book's name\" value='"
                + book.getName() + "' required><br/>\r\n"
                + "                    <label for=\"bookAuthor\">Book Author : </label><input type=\"text\" name=\"author\" id=\"bookAuthor\" placeholder=\"Enter Author's Name\" value='"
                + book.getAuthor() + "' required><br/>\r\n"
                + "                    <label for=\"bookPrice\">Book Price : </label><input type=\"number\" name=\"price\" placeholder=\"Enter the Price\" value='"
                + book.getPrice() + "' required><br/>\r\n"
                + "                    <label for=\"bookQuantity\">Book Qnty : </label><input type=\"number\" name=\"quantity\" id=\"bookQuantity\" placeholder=\"Enter the quantity\" value='"
                + book.getQuantity() + "' required><br/>\r\n"
                + "                    <input class=\"btn btn-success my-2\" type=\"submit\" name='updateFormSubmitted' value=\" Update Book \">\r\n"
                + "                </form>\r\n"
                + "            </td>\r\n"
                + "        </tr>  \r\n"
                + "    </table>";
        pw.println(form);
    }
}

