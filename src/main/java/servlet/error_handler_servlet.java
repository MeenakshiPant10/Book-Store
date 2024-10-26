package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.learn.constant.response_code;
import com.learn.model.store_exception;
import com.learn.model.user_role;
import com.learn.util.store_util;

public class error_handler_servlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Fetch the exceptions
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
        String errorMessage = response_code.INTERNAL_SERVER_ERROR.getMessage();
        String errorCode = response_code.INTERNAL_SERVER_ERROR.name();

        if (statusCode == null)
            statusCode = 0;
        Optional<response_code> errorCodes = response_code.getMessageByStatusCode(statusCode);
        if (errorCodes.isPresent()) {
            errorMessage = errorCodes.get().getMessage();
            errorCode = errorCodes.get().name();
        }

        if (throwable != null && throwable instanceof store_exception) {
            store_exception storeException = (store_exception) throwable;
            if (storeException != null) {
                errorMessage = storeException.getMessage();
                statusCode = storeException.getStatusCode();
                errorCode = storeException.getErrorCode();
                storeException.printStackTrace();
            }
        }

        System.out.println("======ERROR TRIGGERED========");
        System.out.println("Servlet Name: " + servletName);
        System.out.println("Request URI: " + requestUri);
        System.out.println("Status Code: " + statusCode);
        System.out.println("Error Code: " + errorCode);
        System.out.println("Error Message: " + errorMessage);
        System.out.println("=============================");

        if (store_util.isLoggedIn(user_role.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("customer_home.html");
            rd.include(req, res);
            store_util.setActiveTab(pw, "home");
            showErrorMessage(pw, errorCode, errorMessage);

        } else if (store_util.isLoggedIn(user_role.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("seller_home.html");
            rd.include(req, res);
            store_util.setActiveTab(pw, "home");
            showErrorMessage(pw, errorCode, errorMessage);

        } else {
            RequestDispatcher rd = req.getRequestDispatcher("index.html");
            rd.include(req, res);
            pw.println("<script>"
                    + "document.getElementById('topmid').innerHTML='';"
                    + "document.getElementById('happy').innerHTML='';"
                    + "</script>");
            showErrorMessage(pw, errorCode, errorMessage);
        }

    }

    private void showErrorMessage(PrintWriter pw, String errorCode, String errorMessage) {
        pw.println("<div class='container my-5'>"
                + "<div class=\"alert alert-success\" role=\"alert\" style='max-width:450px; text-align:center; margin:auto;'>\r\n"
                + "  <h4 class=\"alert-heading\">"
                + errorCode
                + "</h4>\r\n"
                + "  <hr>\r\n"
                + "  <p class=\"mb-0\">"
                + errorMessage
                + "</p>\r\n"
                + "</div>"
                + "</div>");

    }

}
