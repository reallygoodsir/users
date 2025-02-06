package org.services;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.application.ApplicationLoader;
import org.converters.ErrorDetailsConverter;
import org.converters.UserJsonConverter;
import org.models.ErrorDetails;
import org.models.User;
import org.models.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserServiceImpl implements UserService {

    public User create(User user) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(ApplicationLoader.loadApplication().getUserServiceUrl());
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, ApplicationLoader.loadApplication().getUserServiceAuthorizationToken());
            UserJsonConverter userJsonConverter = new UserJsonConverter();
            String jsonUserPayload = userJsonConverter.convertUserToJson(user);
            StringEntity entity = new StringEntity(jsonUserPayload);
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                if (response.getCode() == HttpStatus.SC_OK) {
                    String responseJson = getBody(response);
                    return userJsonConverter.convert(responseJson);
                } else {
                    String responseJson = getBody(response);
                    ErrorDetailsConverter errorDetailsConverter = new ErrorDetailsConverter();
                    ErrorDetails errorDetails = errorDetailsConverter.convert(responseJson);
                    String errorMessage = errorDetails.getErrorMessage();
                    throw new Exception(errorMessage);
                }
            }
        }
    }

    @Override
    public User update(User user) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut putRequest = new HttpPut(ApplicationLoader.loadApplication().getUserServiceUrl());
            putRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON);
            putRequest.setHeader(HttpHeaders.AUTHORIZATION, ApplicationLoader.loadApplication().getUserServiceAuthorizationToken());
            UserJsonConverter userJsonConverter = new UserJsonConverter();
            String jsonUserPayload = userJsonConverter.convertUserToJson(user);
            StringEntity entity = new StringEntity(jsonUserPayload);
            putRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
                if (response.getCode() == HttpStatus.SC_OK) {
                    String responseJson = getBody(response);
                    return userJsonConverter.convert(responseJson);
                } else {
                    String responseJson = getBody(response);
                    ErrorDetailsConverter errorDetailsConverter = new ErrorDetailsConverter();
                    ErrorDetails errorDetails = errorDetailsConverter.convert(responseJson);
                    String errorMessage = errorDetails.getErrorMessage();
                    throw new Exception(errorMessage);
                }
            }
        }
    }

    @Override
    public User load(int id) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = ApplicationLoader.loadApplication().getUserServiceUrl() + "?id=" + id;
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, ApplicationLoader.loadApplication().getUserServiceAuthorizationToken());
            try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
                if (response.getCode() == HttpStatus.SC_OK) {
                    String responseJson = getBody(response);
                    UserJsonConverter userJsonConverter = new UserJsonConverter();
                    return userJsonConverter.convert(responseJson);
                } else {
                    String responseJson = getBody(response);
                    ErrorDetailsConverter errorDetailsConverter = new ErrorDetailsConverter();
                    ErrorDetails errorDetails = errorDetailsConverter.convert(responseJson);
                    String errorMessage = errorDetails.getErrorMessage();
                    throw new Exception(errorMessage);
                }
            }
        }
    }

    @Override
    public Users loadAll() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(ApplicationLoader.loadApplication().getUserServiceUrl());
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, ApplicationLoader.loadApplication().getUserServiceAuthorizationToken());
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getCode() == HttpStatus.SC_OK) {
                    String responseJson = getBody(response);
                    UserJsonConverter userJsonConverter = new UserJsonConverter();
                    return userJsonConverter.convertJsonToUsers(responseJson);
                } else {
                    String responseJson = getBody(response);
                    ErrorDetailsConverter errorDetailsConverter = new ErrorDetailsConverter();
                    ErrorDetails errorDetails = errorDetailsConverter.convert(responseJson);
                    String errorMessage = errorDetails.getErrorMessage();
                    throw new Exception(errorMessage);
                }
            }
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = ApplicationLoader.loadApplication().getUserServiceUrl() + "?id=" + id;
            HttpDelete deleteRequest = new HttpDelete(url);
            deleteRequest.setHeader(HttpHeaders.AUTHORIZATION, ApplicationLoader.loadApplication().getUserServiceAuthorizationToken());
            try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                if (response.getCode() == HttpStatus.SC_OK) {
                    return true;
                } else {
                    String responseJson = getBody(response);
                    ErrorDetailsConverter errorDetailsConverter = new ErrorDetailsConverter();
                    ErrorDetails errorDetails = errorDetailsConverter.convert(responseJson);
                    String errorMessage = errorDetails.getErrorMessage();
                    throw new Exception(errorMessage);
                }
            }
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
