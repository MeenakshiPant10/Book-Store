package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.learn.constant.book_store;
import com.learn.model.book;
import com.learn.model.cart;
import com.learn.model.user_role;
import com.learn.service.book_service;
import com.learn.service_implementation.book_service_impl;
import com.learn.util.store_util;

public class cart_servlet extends HttpServlet {

    book_service bookService = new book_service_impl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        // Check if Customer is logged In
        if (!store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("customer_login.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            // Add/Remove Item from the cart if requested
            store_util.updateCartItems(req);

            HttpSession session = req.getSession();
            String bookIds = (session.getAttribute("items") != null) ? (String) session.getAttribute("items") : ""; // get bookIds from session

            if (bookIds.isEmpty()) {
                pw.println("<div style='color:red;'>Your cart is empty.</div>");
                return;
            }

            RequestDispatcher rd = req.getRequestDispatcher("customer_home.html");
            rd.include(req, res);

            // Set the active tab as cart
            store_util.setActiveTab(pw, "cart");

            // Fetch books from the database based on bookIds
            List<book> books = bookService.getBooksByCommaSeperatedBookIds(bookIds);
            List<cart> cartItems = new ArrayList<>();
            pw.println("<div id='topmid' style='background-color:grey'>Shopping Cart</div>");
            pw.println("<table class=\"table table-hover\" style='background-color:white'>\r\n"
                    + "  <thead>\r\n"
                    + "    <tr style='background-color:black; color:white;'>\r\n"
                    + "      <th scope=\"col\">BookId</th>\r\n"
                    + "      <th scope=\"col\">Name</th>\r\n"
                    + "      <th scope=\"col\">Author</th>\r\n"
                    + "      <th scope=\"col\">Price/Item</th>\r\n"
                    + "      <th scope=\"col\">Quantity</th>\r\n"
                    + "      <th scope=\"col\">Amount</th>\r\n"
                    + "    </tr>\r\n"
                    + "  </thead>\r\n"
                    + "  <tbody>\r\n");

            double amountToPay = 0;
            if (books == null || books.isEmpty()) {
                pw.println("    <tr style='background-color:green'>\r\n"
                        + "      <th scope=\"row\" colspan='6' style='color:yellow; text-align:center;'> No Items In the Cart </th>\r\n"
                        + "    </tr>\r\n");
            } else {
                for (book book : books) {
                    int qty = session.getAttribute("qty_" + book.getBarcode()) != null ? (int) session.getAttribute("qty_" + book.getBarcode()) : 0;
                    cart cart = new cart(book, qty);
                    cartItems.add(cart);
                    amountToPay += (qty * book.getPrice());
                    pw.println(getRowData(cart));
                }

                // set cartItems and amountToPay in the session
                session.setAttribute("cartItems", cartItems);
                session.setAttribute("amountToPay", amountToPay);

                if (amountToPay > 0) {
                    pw.println("    <tr style='background-color:green'>\r\n"
                            + "      <th scope=\"row\" colspan='5' style='color:yellow; text-align:center;'> Total Amount To Pay </th>\r\n"
                            + "      <td colspan='1' style='color:white; font-weight:bold'><span>&#8377;</span> "
                            + amountToPay
                            + "</td>\r\n"
                            + "    </tr>\r\n");
                    pw.println("<div style='text-align:right; margin-right:20px;'>\r\n"
                            + "<form action=\"checkout\" method=\"post\">"
                            + "<input type='submit' class=\"btn btn-primary\" name='pay' value='Proceed to Pay &#8377; "
                            + amountToPay + "'/></form>"
                            + "    </div>");
                }
            }
            pw.println("  </tbody>\r\n"
                    + "</table>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div class='alert alert-danger'>An error occurred while processing your request. Please try again later.</div>");
        }
    }

    public String getRowData(cart cart) {
        book book = cart.getBook();
        return "    <tr>\r\n"
                + "      <th scope=\"row\">" + book.getBarcode() + "</th>\r\n"
                + "      <td>" + book.getName() + "</td>\r\n"
                + "      <td>" + book.getAuthor() + "</td>\r\n"
                + "      <td><span>&#8377;</span> " + book.getPrice() + "</td>\r\n"
                + "      <td><form method='post' action='cart'><button type='submit' name='removeFromCart' class=\"glyphicon glyphicon-minus btn btn-danger\"></button> "
                + "<input type='hidden' name='selectedBookId' value='" + book.getBarcode() + "'/>"
                + cart.getQuantity()
                + " <button type='submit' name='addToCart' class=\"glyphicon glyphicon-plus btn btn-success\"></button></form></td>\r\n"
                + "      <td><span>&#8377;</span> " + (book.getPrice() * cart.getQuantity()) + "</td>\r\n"
                + "    </tr>\r\n";
    }
}
