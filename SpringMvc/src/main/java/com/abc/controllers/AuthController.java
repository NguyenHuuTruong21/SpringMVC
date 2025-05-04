package com.abc.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.entities.Province;
import com.abc.entities.User;
import com.abc.services.ProvinceService;
import com.abc.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private UserService userService;
    private ProvinceService provinceService;

    @Autowired
    public AuthController(UserService userService, ProvinceService provinceService) {
        this.userService = userService;
        this.provinceService = provinceService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = userService.getUserByUserName(username);

        if (user != null && user.getPassWord().equals(password)) {
            session.setAttribute("user", user);
            session.setAttribute("user_id", user.getId());
            return "redirect:/";
        }

        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<Province> provinces = provinceService.getAllProvinces();
        model.addAttribute("provinces", provinces);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("provinceId") int provinceId,
            Model model) {

        // Kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            model.addAttribute("error", "Email không hợp lệ!");
            return "register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (userService.isEmailExists(email)) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }

        // Tạo đối tượng User
        User user = new User(username, password, email);

        // Kiểm tra ngày sinh
        LocalDate dob = LocalDate.parse(dateOfBirth);
        LocalDate now = LocalDate.now();
        int age = Period.between(dob, now).getYears();
        if (age < 15) {
            model.addAttribute("error", "Bạn phải trên 15 tuổi để đăng ký!");
            return "register";
        }

        user.setDateOfBirth(dob);
        user.setProvinceId(provinceId);

        // Đăng ký người dùng
        if (userService.registerUser(user)) {
            return "redirect:/login";
        }

        model.addAttribute("error", "Đăng ký thất bại");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}