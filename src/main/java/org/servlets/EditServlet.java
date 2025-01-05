package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.models.User;
import org.parsers.UserParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idParameter = request.getParameter("id");
            if (idParameter == null) {
                throw new Exception("No id provided");
            }
            int id = Integer.parseInt(idParameter);

            User user;
            try (
                    CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = "http://localhost:8080/user-service/user-management?id=" + id;
                HttpGet getRequest = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                    String responseJson = getBody(response);
                    UserParser userParser = new UserParser();
                    user = userParser.convert(responseJson);
                }
            }
            if (user != null) {
                resp.setContentType("text/html");
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
            System.err.println("Error in edit servlet doGet");
            exception.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String idParameter = request.getParameter("id");
            int id = Integer.parseInt(idParameter);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            String birthDateString = request.getParameter("birthDate");
            Date birthDate = dateFormat.parse(birthDateString);
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            User user = new User(id, name, age, birthDate);
            int status;
            try (
                    CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = "http://localhost:8080/user-service/user-management?id=" + id;
                HttpPut putRequest = new HttpPut(url);

                UserParser userParser = new UserParser();
                String json = userParser.convertUserToJson(user);
                putRequest.setHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity(json);
                putRequest.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
                    status = response.getCode();
                }
            }catch (Exception exception){
                status = 500;
            }
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            if (status != 500) {
                resp.setStatus(resp.SC_MOVED_TEMPORARILY);
                resp.setHeader("Location", "http://localhost:8080/users/view-servlet");
            } else {
                out.println("Sorry! unable to update record");
            }

            out.close();
        } catch (Exception exception) {
            System.err.println("Error in edit servlet doPost");
            exception.printStackTrace();
        }
    }

    private String getBody(CloseableHttpResponse response) throws IOException {
        StringBuilder responseBody = new StringBuilder();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
        }
        return responseBody.toString();
    }
}
