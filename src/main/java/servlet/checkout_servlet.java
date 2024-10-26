package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.book_store;
import com.learn.model.user_role;
import com.learn.util.store_util;

public class checkout_servlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);

        // Check if the user is logged in as a customer
        if (!store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("customer_login.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning'>Please login to continue.</div>");
            return;
        }

        try {
            // Fetch the total amount to pay from the session
            Double amountToPay = (Double) req.getSession().getAttribute("amountToPay");
            if (amountToPay == null || amountToPay <= 0) {
                pw.println("<div class='alert alert-danger'>No items in the cart or invalid amount to pay.</div>");
                return;
            }

            // Load the payment page
            RequestDispatcher rd = req.getRequestDispatcher("payment.html");
            rd.include(req, res);

            // Set the active tab as cart
            store_util.setActiveTab(pw, "cart");

            // Display the total amount to pay on the payment page
            pw.println("<div class='payment-details'>");
            pw.println("<h3>Total Amount: <span style=\"color: black\"><b>&#8377; " + amountToPay + "</b></span></h3>");
            pw.println("<form action=\"confirmOrder\" method=\"post\">");
            pw.println("<input type=\"submit\" value=\"Pay & Place Order\" class=\"btn btn-primary\">");
            pw.println("</form>");
            pw.println("</div>");
            
        } catch (Exception e) {
            e.printStackTrace();  // Log the error in the server console
            pw.println("<div class='alert alert-danger'>An error occurred while processing your request. Please try again later.</div>");
        }
    }
}
