package by.bsuir.service;

import by.bsuir.entity.User;

public interface UserService {

    User getUserByUsername(String username);

    User save(User user);

}
