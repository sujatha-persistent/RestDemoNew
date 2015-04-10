package com.oauth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/DemoRest"})
public class DemoRest extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";

	protected void doGet(HttpServletRequest request, HttpServletResponse  response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);
		String instanceUrl = (String) request.getSession().getAttribute(INSTANCE_URL);

		if (accessToken == null) {
			writer.write("Error ­ no access token");
			return;
		}
		writer.write("We have an access token: " + accessToken + "\n" + "Using instance " + instanceUrl);
	}
}
