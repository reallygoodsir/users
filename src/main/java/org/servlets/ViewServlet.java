package org.servlets;

import org.apache.hc.core5.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.models.User;
import org.models.Users;
import org.services.UserService;
import org.services.UserServiceImpl;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ViewServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            UserService userService = new UserServiceImpl();
            Users users = userService.loadAll();
            if (users != null) {
                List<User> list = users.getUsers();
                resp.setContentType(ContentType.TEXT_HTML.toString());
                PrintWriter out = resp.getWriter();
                out.println("<a href='save-servlet'>Add New Employee</a>");
                out.println("<h1>Employees List</h1>");
                out.print("<table border='1' width='100%'");
                out.print("<tr><th>Id</th><th>Name</th><th>Age</th><th>Birth Date</th> <th>Edit</th><th>Delete</th></tr>");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (User user : list) {
                    Date birthDate = user.getBirthDate();
                    String formattedBirthDate = dateFormat.format(birthDate);
                    out.print("<tr>" +
                            "<td>" + user.getId() + "</td>" +
                            "<td>" + user.getName() + "</td>" +
                            "<td>" + user.getAge() + "</td>" +
                            "<td>" + formattedBirthDate + "</td>" +
                            "<td><a href='edit-servlet?id=" + user.getId() + "'>edit</a></td>" +
                            "<td><a href='delete-servlet?id=" + user.getId() + "'>delete</a></td>" +
                            "</tr>");
                }
                out.print("</table>");
                out.close();
            }
        } catch (Exception exception) {
            LOGGER.error("Error in view servlet", exception);
        }
    }
}
