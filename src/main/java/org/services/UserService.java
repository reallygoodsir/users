package org.services;

import org.models.User;
import org.models.Users;

public interface UserService {
    User create(User user) throws Exception;

    User update(User user) throws Exception;

    User load(int id) throws Exception;

    Users loadAll() throws Exception;

    boolean delete(int id) throws Exception;
}
