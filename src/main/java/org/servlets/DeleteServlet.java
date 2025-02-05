package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            String idReqParam = request.getParameter("id");
            int id = Integer.parseInt(idReqParam);
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = "http://localhost:8080/user-service/user-management?id=" + id;
                HttpDelete deleteRequest = new HttpDelete(url);
                deleteRequest.setHeader("Authorization", "1234567890qawsedrftgthyujkiol");
                try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                    if (response.getCode() == 200) {
                        resp.sendRedirect("view-servlet");
                    } else if (response.getCode() == 401) {
                        throw new Exception("Not authorize to call user service");
                    } else {
                        throw new Exception("Unexpected error occurred");
                    }
                }
            }
        } catch (Exception exception) {
            System.err.println("Error in delete servlet");
            exception.printStackTrace();
        }
    }
}
