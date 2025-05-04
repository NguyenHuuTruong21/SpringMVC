package com.abc.services;

import java.util.List;

import com.abc.entities.User;

public interface UserService {
    User getUserByUserName(String userName);
    boolean registerUser(User user);
    boolean updateUserProfile(User user);
    List<User> searchUsersByFollowCount(int minFollowing, int minFollowers);
    boolean isEmailExists(String email); // Thêm phương thức kiểm tra email
}