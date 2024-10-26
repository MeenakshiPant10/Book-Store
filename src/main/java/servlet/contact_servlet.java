package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.learn.service.contact_service;
import com.learn.service_implementation.contact_service_impl;

import com.learn.constant.book_store;
import com.learn.constant.response_code;
import com.learn.constant.db.add_contact_db;
import com.learn.model.contact;
import com.learn.model.user_role;

public class contact_servlet extends HttpServlet {
	contact_service contact_Service = new contact_service_impl();

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter pw = res.getWriter();
		res.setContentType(book_store.CONTENT_TYPE_TEXT_HTML);
		String mailId = req.getParameter(add_contact_db.COLUMN_MAILID);
		String fName = req.getParameter(add_contact_db.COLUMN_FIRSTNAME);
		String lName = req.getParameter(add_contact_db.COLUMN_LASTNAME);
		String subject = req.getParameter(add_contact_db.COLUMN_SUBJECT);
		String phone = req.getParameter(add_contact_db.COLUMN_PHONE);
		contact cont = new contact();
		cont.setEmailId(mailId);
		cont.setFirstName(fName);
		cont.setLastName(lName);
		cont.setSubject(subject);
		cont.setphone(Long.parseLong(phone));

		try {
			String respCode = contact_Service.AddContact(cont);
			System.out.println(respCode);
			if (response_code.SUCCESS.name().equalsIgnoreCase(respCode)) {

				pw.println("<table class=\"tab\"><tr><td>Contacted Successfully</td></tr></table>");
			} else {

				pw.println("<table class=\"tab\"><tr><td>" + respCode + "</td></tr></table>");
				pw.println("Sorry for interruption! Try again");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
