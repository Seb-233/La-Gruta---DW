package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // -------- LOGIN --------
    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session) {
        ModelAndView mv = new ModelAndView();

        User user = userService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Guardamos el usuario en la sesión
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId()); // necesario para el header
            mv.setViewName("redirect:/la_gruta/index");
            mv.addObject("message", "Login exitoso");
        } else {
            mv.setViewName("login");
            mv.addObject("error", "Usuario o contraseña incorrectos");
        }
        return mv;
    }

    // -------- USUARIOS (ADMIN) --------
    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.SearchAll());
        return "users"; // templates/users.html
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form"; // crea plantilla user-form.html
    }

    @PostMapping("/users/save")
    public String saveUser(User user) {
        userService.add(user);
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.SearchById(id);
        model.addAttribute("user", user);
        return "user-form"; // misma plantilla, pero con datos
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    // -------- REGISTRO --------
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
public String registerUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           HttpSession session) {
    User user = new User(username, password);
    user.setRole(role);
    userService.add(user);

    // Guardamos el usuario en sesión directamente
    session.setAttribute("username", user.getUsername());
    session.setAttribute("userId", user.getId());

    if ("ADMIN".equals(role)) {
        return "redirect:/users";
    } else {
        return "redirect:/la_gruta/index"; 
    }
}


    // -------- PERFIL --------
    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");

        if (username == null || userId == null) {
            return "redirect:/la_gruta/login"; // si no está logueado
        }

        User user = userService.SearchById(userId);
        model.addAttribute("user", user);
        return "profile"; // templates/profile.html
    }

    @GetMapping("/profile/edit/{id}")
    public String editProfileForm(@PathVariable Long id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");

        if (username == null || userId == null) {
            return "redirect:/la_gruta/login";
        }

        // Validamos que el usuario solo pueda editar su propio perfil
        if (!userId.equals(id)) {
            return "redirect:/profile";
        }

        User user = userService.SearchById(id);
        model.addAttribute("user", user);
        return "edit-profile"; // templates/edit-profile.html
    }

    @PostMapping("/profile/save")
    public String saveProfile(User user, HttpSession session) {
        userService.update(user);

        // actualizar sesión si cambió el nombre de usuario
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getId());

        return "redirect:/la_gruta/index";
    }
}
