package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String url = "http://localhost:8080/user-service/user-management";

            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String birthDateString = request.getParameter("birthDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Adjust the pattern based on the incoming format

            // Parse the string to LocalDate
            LocalDate localDate = LocalDate.parse(birthDateString, formatter);

            // Convert LocalDate to java.util.Date
            Date birthDate = java.sql.Date.valueOf(localDate);
            User user = new User();
            user.setName(name);
            user.setAge(age);
            user.setBirthDate(birthDate);

            UserParser userParser = new UserParser();
            String json = userParser.convertUserToJson(user);
            try (
                    CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost getRequest = new HttpPost(url);
                getRequest.setHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity(json);
                getRequest.setEntity(entity);
                try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                    String responseJson = getBody(response);
                    User responseUser = userParser.convert(responseJson);
                    System.out.println("\n" + responseUser + "\n");
                    if (response.getCode() == 200) {
                        System.out.println("200");
                        resp.sendRedirect("view-servlet");
                    } else if (response.getCode() == 500) {
                        System.out.println("500");
                        throw new Exception("Error in user-service");
                    }
                }
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
