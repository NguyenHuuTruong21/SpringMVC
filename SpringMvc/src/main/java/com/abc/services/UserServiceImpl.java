package com.abc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.UserDAO;
import com.abc.entities.User;

@Service 
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUserByUserName(String userName) {
        return userDAO.getUserByUserName(userName);
    }

    @Override
    public boolean registerUser(User user) {
        return userDAO.registerUser(user);
    }

    @Override
    public List<User> searchUsersByFollowCount(int minFollowing, int minFollowers) {
        return userDAO.searchUsersByFollowCount(minFollowing, minFollowers);
    }

    @Override
    public boolean updateUserProfile(User user) {
        return userDAO.updateUserProfile(user);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userDAO.isEmailExists(email);
    }
}