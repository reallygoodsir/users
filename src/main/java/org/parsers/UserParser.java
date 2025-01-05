package org.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.models.User;
import org.models.Users;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserParser {
    public String convertUserToJson(User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(user);
        } catch (IOException exception) {
            System.err.println("Error converting User to JSON\n" + exception.getMessage());
            throw new RuntimeException("Error in JSON Converter");
        }
    }

    public User convert(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            return objectMapper.readValue(jsonContent, User.class);
        } catch (IOException exception) {
            System.err.println("Error parsing JSON content\n" + exception.getMessage());
            throw new RuntimeException("Error in JSON Parser");
        }
    }
    public Users convertJsonToUsers(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
            return objectMapper.readValue(jsonContent, Users.class);
        } catch (IOException exception) {
            System.err.println("Error converting JSON to Users object\n" + exception.getMessage());
            throw new RuntimeException("Error in JSON to Users conversion", exception);
        }
    }
}
