package fi.muke.jeitodo.service;

import fi.muke.jeitodo.domain.User;

public interface UserService {
    public User findUserByEmail(String email);

    public void saveUser(User user);
}
