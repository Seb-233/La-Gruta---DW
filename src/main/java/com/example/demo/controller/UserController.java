package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/la_gruta")
public class UserController {

    @Autowired
    private UserService userService;

@PostMapping("/login")
public ModelAndView login(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {
    ModelAndView mv = new ModelAndView();
    User user = userService.findByUsername(username);

    if (user != null && user.getPassword().equals(password)) {
        // Guardar datos en sesión
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole()); // NUEVO: guardar rol

        // Redirigir según rol
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            mv.setViewName("redirect:/la_gruta/comidas/admin"); // Tabla de admin
        } else {
            mv.setViewName("redirect:/la_gruta/index"); // Página cliente
        }

        mv.addObject("message", "Login exitoso");
    } else {
        mv.setViewName("login");
        mv.addObject("error", "Usuario o contraseña incorrectos");
    }
    return mv;
}


    @GetMapping("/user-login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               HttpSession session) {
        User user = new User(username, password);
        user.setRole(role);
        userService.add(user);

        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getId());

        if ("ADMIN".equals(role)) {
            return "redirect:/la_gruta/users";
        } else {
            return "redirect:/la_gruta/index";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");
        if (username == null || userId == null) return "redirect:/la_gruta/login";
        User user = userService.SearchById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String editProfileForm(@PathVariable Long id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");
        if (username == null || userId == null) return "redirect:/la_gruta/login";
        if (!userId.equals(id)) return "redirect:/la_gruta/profile";
        User user = userService.SearchById(id);
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/save")
    public String saveProfile(User user, HttpSession session) {
        userService.update(user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getId());
        return "redirect:/la_gruta/index";
    }

    // Admin Users
    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.SearchAll());
        return "users";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user) {
        userService.add(user);
        return "redirect:/la_gruta/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.SearchById(id);
        model.addAttribute("user", user);
        return "user-form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/la_gruta/users";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra la sesión
        return "redirect:/la_gruta/index";
    }

}
