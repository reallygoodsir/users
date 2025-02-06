package org.servlets;

import org.apache.hc.core5.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.converters.UserRequestConverter;
import org.models.User;
import org.services.UserService;
import org.services.UserServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(SaveServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"ISO-8859-1\">\n" +
                "    <title>Insert title here</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Add New Employee</h1>\n" +
                "<form action=\"save-servlet\" method=\"post\">\n" +
                "    <table>\n" +
                "        <tr><td>Name:</td><td><input type=\"text\" name=\"name\"/></td></tr>\n" +
                "        <tr><td>Age:</td><td><input type=\"text\" name=\"age\"/></td></tr>\n" +
                "        <tr><td>Birth Date:</td><td><input type=\"date\" name=\"birthDate\"/></td></tr>\n" +
                "        <tr><td colspan=\"2\"><input type=\"submit\" value=\"Save Employee\"/></td></tr>\n" +
                "    </table>\n" +
                "</form>\n" +
                "\n" +
                "<br/>\n" +
                "<a href=\"view-servlet\">View Employees</a>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String nameReqParam = request.getParameter("name");
            String ageReqParam = request.getParameter("age");
            String birthDateReqParam = request.getParameter("birthDate");

            UserRequestConverter userRequestConverter = new UserRequestConverter();
            User user = userRequestConverter.convert(nameReqParam, ageReqParam, birthDateReqParam);

            UserService userService = new UserServiceImpl();
            User createdUser = userService.create(user);

            if (createdUser.getId() > 0) {
                LOGGER.info("Successfully created user {}", createdUser.getId());
            } else {
                LOGGER.error("Created user id is invalid {}", createdUser.getId());
            }
            resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            resp.setHeader(HttpHeaders.LOCATION, "http://localhost:8080/users/view-servlet");
        } catch (Exception exception) {
            LOGGER.error("Error in save servlet", exception);
        }
    }
}
