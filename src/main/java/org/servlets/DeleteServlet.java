package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.parsers.UserParser;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String idParameter = request.getParameter("id");
            int id = Integer.parseInt(idParameter);
            try (
                    CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = "http://localhost:8080/user-service/user-management?id=" + id;
                HttpDelete deleteRequest = new HttpDelete(url);
                try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                    if(response.getCode() != 500){
                        resp.sendRedirect("view-servlet");
                    }
                }
            }
        } catch (Exception exception) {
            System.err.println("Error in delete servlet");
            exception.printStackTrace();
        }
    }
}
