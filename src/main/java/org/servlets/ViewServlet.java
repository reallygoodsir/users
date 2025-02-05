package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.models.User;
import org.models.Users;
import org.converters.UserJsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
        try {
            Users users;
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String url = "http://localhost:8080/user-service/user-management";
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Authorization", "1234567890qawsedrftgthyujkiol");
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int code = response.getCode();
                    if (code == 200) {
                        String responseJson = getBody(response);
                        UserJsonConverter userJsonConverter = new UserJsonConverter();
                        users = userJsonConverter.convertJsonToUsers(responseJson);
                    } else if (response.getCode() == 401) {
                        System.out.println("Not authorize to make a call");
                        throw new Exception("Not authorize to call user service");
                    } else {
                        System.out.println("Unexpected error occurred calling user service");
                        throw new Exception("Unexpected error occurred");
                    }

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
            System.err.println("error");
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
