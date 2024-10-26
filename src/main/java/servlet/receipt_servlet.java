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

public class receipt_servlet extends HttpServlet {
    book_service bookService = new book_service_impl();

    //NOT_IN_USED
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);
        if (!store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("customer_login.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            List<book> books = bookService.getAllBooks();
            int i = 0;
            RequestDispatcher rd = req.getRequestDispatcher("customer_home.html");
            rd.include(req, res);
            store_util.setActiveTab(pw, "cart");
            pw.println("<div class=\"tab\">Your order status is as below</div>");
            pw.println(
                    "<div class=\"tab\">\r\n" + "		<table>\r\n" + "			<tr>\r\n" + "				\r\n"
                            + "				<th>Book Code</th>\r\n" + "				<th>Book Name</th>\r\n"
                            + "				<th>Book Author</th>\r\n" + "				<th>Book Price</th>\r\n"
                            + "				<th>Quantity</th><br/>\r\n" + "				<th>Amount</th><br/>\r\n"
                            + "			</tr>");
            double total = 0.0;
            for (book book : books) {
                double bPrice = book.getPrice();
                String bCode = book.getBarcode();
                String bName = book.getName();
                String bAuthor = book.getAuthor();
                int bQty = book.getQuantity();
                i = i + 1;

                String qt = "qty" + Integer.toString(i);
                int quantity = Integer.parseInt(req.getParameter(qt));
                try {
                    String check1 = "checked" + Integer.toString(i);
                    String getChecked = req.getParameter(check1);
                    if (bQty < quantity) {
                        pw.println(
                                "</table><div class=\"tab\" style='color:red;'>Please Select the Qty less than Available Books Quantity</div>");
                        break;
                    }

                    if (getChecked.equals("pay")) {
                        pw.println("<tr><td>" + bCode + "</td>");
                        pw.println("<td>" + bName + "</td>");
                        pw.println("<td>" + bAuthor + "</td>");
                        pw.println("<td>" + bPrice + "</td>");
                        pw.println("<td>" + quantity + "</td>");
                        double amount = bPrice * quantity;
                        total = total + amount;
                        pw.println("<td>" + amount + "</td></tr>");
                        bQty = bQty - quantity;
                        System.out.println(bQty);
                        bookService.updateBookQtyById(bCode, bQty);
                    }
                } catch (Exception e) {
                }
            }
            pw.println("</table><br/><div class='tab'>Total Paid Amount: " + total + "</div>");
//            String fPay = req.getParameter("f_pay");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

