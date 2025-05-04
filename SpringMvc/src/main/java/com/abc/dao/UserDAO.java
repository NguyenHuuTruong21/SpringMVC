package com.abc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.abc.config.DatabaseConfig;
import com.abc.entities.User;

@Repository
public class UserDAO {

    // Kiểm tra email đã tồn tại chưa
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUserName(String userName) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("created_at"),
                    rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
                    rs.getInt("province_id"),
                    rs.getString("avatar")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserProfile(User user) {
        String sql = "UPDATE users SET email = ?, date_of_birth = ?, province_id = ?, avatar = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setObject(2, user.getDateOfBirth());
            stmt.setInt(3, user.getProvinceId());
            stmt.setString(4, user.getAvatar());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, email, date_of_birth, province_id, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassWord());
            stmt.setString(3, user.getEmail());
            stmt.setObject(4, user.getDateOfBirth());
            stmt.setInt(5, user.getProvinceId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> searchUsersByFollowCount(int minFollowing, int minFollowers) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* FROM users u " +
                    "WHERE (SELECT COUNT(*) FROM follows WHERE following_user_id = u.id) >= ? " +
                    "AND (SELECT COUNT(*) FROM follows WHERE followed_user_id = u.id) >= ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minFollowing);
            stmt.setInt(2, minFollowers);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("created_at"),
                    rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
                    rs.getInt("province_id"),
                    rs.getString("avatar")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}