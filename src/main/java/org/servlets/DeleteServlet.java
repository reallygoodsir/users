package org.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.services.UserService;
import org.services.UserServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String idReqParam = request.getParameter("id");
            int id = Integer.parseInt(idReqParam);
            UserService userService = new UserServiceImpl();
            boolean isSuccessful = userService.delete(id);
            if (isSuccessful) {
                resp.sendRedirect("view-servlet");
            }
        } catch (Exception exception) {
            LOGGER.error("Error in delete servlet", exception);
        }
    }
}
