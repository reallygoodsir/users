package org.servlets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.converters.UserRequestConverter;
import org.models.User;
import org.converters.UserJsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveServlet extends HttpServlet {
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

            // skip validation
            // convert request data to object (user)
            UserRequestConverter userRequestConverter = new UserRequestConverter();
            User user = userRequestConverter.convert(nameReqParam, ageReqParam, birthDateReqParam);
            UserJsonConverter userJsonConverter = new UserJsonConverter();
            String jsonUserPayload = userJsonConverter.convertUserToJson(user);


            String url = "http://localhost:8080/user-service/user-management";
            try (
                    CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost postRequest = new HttpPost(url);
                postRequest.setHeader("Content-Type", "application/json");
                postRequest.setHeader("Authorization", "1234567890qawsedrftgthyujkiol");
                StringEntity entity = new StringEntity(jsonUserPayload);
                postRequest.setEntity(entity);
                try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                    if (response.getCode() == 200) {
                        String responseJson = getBody(response);
                        User responseUser = userJsonConverter.convert(responseJson);
                        if (responseUser.getId() > 0) {
                            System.out.println("User successfully created with id " + responseUser.getId());
                            resp.sendRedirect("view-servlet");
                        }
                    } else if (response.getCode() == 401) {
                        System.out.println("Not authorize to make a call");
                        throw new Exception("Not authorize to call user service");
                    } else {
                        System.out.println("Unexpected error occurred calling user service");
                        throw new Exception("Unexpected error occurred");
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
