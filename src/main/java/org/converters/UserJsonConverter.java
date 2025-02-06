package org.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.models.User;
import org.models.Users;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserJsonConverter {
    private static final Logger LOGGER = LogManager.getLogger(UserJsonConverter.class);

    public String convertUserToJson(User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(user);
        } catch (IOException exception) {
            LOGGER.error("Error converting User to JSON", exception);
            throw new RuntimeException("Error in JSON Converter");
        }
    }

    public User convert(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            return objectMapper.readValue(jsonContent, User.class);
        } catch (IOException exception) {
            LOGGER.error("Error parsing JSON content", exception);
            throw new RuntimeException("Error in JSON Parser");
        }
    }

    public Users convertJsonToUsers(String jsonContent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
            return objectMapper.readValue(jsonContent, Users.class);
        } catch (IOException exception) {
            LOGGER.error("Error converting JSON to Users object", exception);
            throw new RuntimeException("Error in JSON to Users conversion", exception);
        }
    }
}
