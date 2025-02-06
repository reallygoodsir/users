package org.servlets;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.converters.UserRequestConverter;
import org.models.User;
import org.services.UserService;
import org.services.UserServiceImpl;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(EditServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String idReqParam = request.getParameter("id");
            if (idReqParam == null || idReqParam.isEmpty()) {
                throw new Exception("No id provided");
            }
            int id = Integer.parseInt(idReqParam);

            UserService userService = new UserServiceImpl();
            User user = userService.load(id);

            if (user != null) {
                resp.setContentType(ContentType.TEXT_HTML.toString());
                PrintWriter out = resp.getWriter();
                out.println("<h1>Update Employee</h1>");
                out.print("<form action='edit-servlet' method='post'>");
                out.print("<table>");
                out.print("<tr><td></td><td><input type='hidden' name='id' value='" + user.getId() + "'/></td></tr>");
                out.print("<tr><td>Name:</td><td><input type='text' name='name' value='" + user.getName() + "'/></td></tr>");
                out.print("<tr><td>Age:</td><td><input type='text' name='age' value='" + user.getAge() + "'/> </td></tr>");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedBirthDate = dateFormat.format(user.getBirthDate());
                out.print("<tr><td>Birth Date:</td><td><input type='date' name='birthDate' value='" + formattedBirthDate + "'/></td></tr>");
                out.print("<tr><td colspan='2'><input type='submit' value='Save Changes'/></td></tr>");
                out.print("</table>");
                out.print("</form>");
                out.close();
            }
        } catch (Exception exception) {
            LOGGER.error("Error in edit servlet doGet", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String idReqParam = request.getParameter("id");
            String nameReqParam = request.getParameter("name");
            String ageReqParam = request.getParameter("age");
            String birthDateReqParam = request.getParameter("birthDate");

            UserRequestConverter userRequestConverter = new UserRequestConverter();
            User user = userRequestConverter.convert(idReqParam, nameReqParam, ageReqParam, birthDateReqParam);

            UserService userService = new UserServiceImpl();
            User updatedUser = userService.update(user);

            if (user.getId() == updatedUser.getId() &&
                    user.getAge() == updatedUser.getAge() &&
                    user.getName().equals(updatedUser.getName()) &&
                    user.getBirthDate().equals(updatedUser.getBirthDate())) {
                LOGGER.info("Successfully updated user {}", updatedUser.getId());
            } else {
                LOGGER.error("Updated user is invalid {}", updatedUser.getId());
            }
            resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            resp.setHeader(HttpHeaders.LOCATION, "http://localhost:8080/users/view-servlet");
        } catch (Exception exception) {
            LOGGER.error("Error in edit servlet doPost", exception);
        }
    }
}
