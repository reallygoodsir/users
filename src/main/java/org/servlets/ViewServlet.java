package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.models.User;
import org.models.Users;
import org.parsers.UserParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        Users users = null;
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = "http://localhost:8080/user-service/user-management";
            HttpGet postRequest = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                String responseJson = getBody(response);
                UserParser userParser = new UserParser();
                users = userParser.convertJsonToUsers(responseJson);

            }
        }
        if (users != null) {
            List<User> list = users.getUsers();
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<a href='save-servlet'>Add New Employee</a>");
            out.println("<h1>Employees List</h1>");
            out.print("<table border='1' width='100%'");
            out.print("<tr><th>Id</th><th>Name</th><th>Age</th><th>Birth Date</th> <th>Edit</th><th>Delete</th></tr>");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (User user : list) {
                Date birthDate = user.getBirthDate();
                String formattedBirthDate = dateFormat.format(birthDate);
                out.print("<tr><td>" + user.getId() + "</td><td>" + user.getName() + "</td><td>" + user.getAge() + "</td> <td>" + formattedBirthDate + "</td> <td><a href='edit-servlet?id=" + user.getId() + "'>edit</a></td> <td><a href='delete-servlet?id=" + user.getId() + "'>delete</a></td></tr>");
            }
            out.print("</table>");

            out.close();
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
