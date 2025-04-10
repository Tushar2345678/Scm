package com.scm.scm20.services;

import com.scm.scm20.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    
    Optional<User> getUserById(String id);
            
    Optional<User> updateUser(User user);
    
    void deleteUser(String id);
    
    boolean isUserExist(String userId);
    
    boolean isUserExistByEmail(String email);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserByUId(String userId);

    User getUserByProviderUserId(String githubId);

    //User getUserByToken(String emailToken);

    
    // add more methods here related user service[logic]
}
