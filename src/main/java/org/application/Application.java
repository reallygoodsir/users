package org.application;

public class Application {
    private final String userServiceUrl;
    private final String userServiceAuthorizationToken;

    public Application(String userServiceUrl, String userServiceAuthorizationToken) {
        this.userServiceUrl = userServiceUrl;
        this.userServiceAuthorizationToken = userServiceAuthorizationToken;
    }

    public String getUserServiceUrl() {
        return userServiceUrl;
    }

    public String getUserServiceAuthorizationToken() {
        return userServiceAuthorizationToken;
    }

    @Override
    public String toString() {
        return "Application{" +
                "userServiceUrl='" + userServiceUrl + '\'' +
                ", userServiceAuthorizationToken='" + userServiceAuthorizationToken + '\'' +
                '}';
    }
}