package com.abc.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.abc.entities.Post;
import com.abc.entities.Province;
import com.abc.entities.User;
import com.abc.services.FollowService;
import com.abc.services.PostService;
import com.abc.services.ProvinceService;
import com.abc.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private PostService postService;
    private UserService userService;
    private FollowService followService;
    private ProvinceService provinceService;

    @Autowired
    public UserController(PostService postService, UserService userService, FollowService followService, ProvinceService provinceService) {
        this.postService = postService;
        this.userService = userService;
        this.followService = followService;
        this.provinceService = provinceService;
    }

    @GetMapping("/profile")
    public String profileUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null)
            return "redirect:/login";

        List<Post> posts = postService.getPostById(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);

        return "profile";
    }

    @GetMapping("/edit-profile")
    public String showEditProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Province> provinces = provinceService.getAllProvinces();
        model.addAttribute("user", user);
        model.addAttribute("provinces", provinces);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("provinceId") int provinceId,
            @RequestParam("avatar") MultipartFile avatarFile,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            model.addAttribute("error", "Email không hợp lệ!");
            return "edit-profile";
        }

        // Kiểm tra email đã tồn tại chưa (trừ email của chính user hiện tại)
        User existingUserWithEmail = userService.getUserByUserName(user.getUsername());
        if (!email.equals(existingUserWithEmail.getEmail()) && userService.isEmailExists(email)) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "edit-profile";
        }

        // Kiểm tra ngày sinh
        LocalDate dob = LocalDate.parse(dateOfBirth);
        LocalDate now = LocalDate.now();
        int age = Period.between(dob, now).getYears();
        if (age < 15) {
            model.addAttribute("error", "Bạn phải trên 15 tuổi!");
            return "edit-profile";
        }

        // Kiểm tra và upload avatar
        String avatarPath = user.getAvatar();
        if (!avatarFile.isEmpty()) {
            String contentType = avatarFile.getContentType();
            if (!(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                model.addAttribute("error", "Avatar phải là file JPG hoặc PNG!");
                return "edit-profile";
            }

            if (avatarFile.getSize() > 200 * 1024) { // 200KB
                model.addAttribute("error", "Avatar không được lớn hơn 200KB!");
                return "edit-profile";
            }

            try {
                String uploadDir = "src/main/webapp/resources/images/avatars/";
                String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, avatarFile.getBytes());
                avatarPath = "/resources/images/avatars/" + fileName;
            } catch (IOException e) {
                model.addAttribute("error", "Lỗi khi upload avatar!");
                return "edit-profile";
            }
        }

        // Cập nhật thông tin user
        user.setEmail(email);
        user.setDateOfBirth(dob);
        user.setProvinceId(provinceId);
        user.setAvatar(avatarPath);

        if (userService.updateUserProfile(user)) {
            session.setAttribute("user", user);
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Cập nhật hồ sơ thất bại!");
            return "edit-profile";
        }
    }

    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(value = "minFollowing", defaultValue = "3") int minFollowing,
            @RequestParam(value = "minFollowers", defaultValue = "5") int minFollowers,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<User> searchResults = userService.searchUsersByFollowCount(minFollowing, minFollowers);
        List<UserFollowStats> userStats = new ArrayList<>();

        if (searchResults.isEmpty()) {
            model.addAttribute("notFound", true);
        } else {
            for (User u : searchResults) {
                int followingCount = followService.countFollowing(u.getId());
                int followerCount = followService.countFollowers(u.getId());
                userStats.add(new UserFollowStats(u, followingCount, followerCount));
            }
            model.addAttribute("searchResults", userStats);
        }

        List<Post> posts = postService.getAllPost(user.getId());
        List<User> userfed = followService.getUserFollowed(user.getId());
        List<User> suggestfollow = followService.getSuggestFollow(user.getId());

        model.addAttribute("posts", posts);
        model.addAttribute("userfed", userfed);
        model.addAttribute("suggestfollow", suggestfollow);

        return "home";
    }

    public static class UserFollowStats {
        private User user;
        private int followingCount;
        private int followerCount;

        public UserFollowStats(User user, int followingCount, int followerCount) {
            this.user = user;
            this.followingCount = followingCount;
            this.followerCount = followerCount;
        }

        public User getUser() {
            return user;
        }

        public int getFollowingCount() {
            return followingCount;
        }

        public int getFollowerCount() {
            return followerCount;
        }
    }
}