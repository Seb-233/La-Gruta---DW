package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username,
            @RequestParam String password) {
        ModelAndView mv = new ModelAndView();

        // Usar el método findByUsername para buscar en DB
        User user = userService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            mv.setViewName("redirect:/la_gruta/index");
            mv.addObject("message", "Login exitoso");
        } else {
            mv.setViewName("login");
            mv.addObject("error", "Usuario o contraseña incorrectos");
        }
        return mv;
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.SearchAll());
        return "users"; // templates/users.html
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String role) {
        User user = new User(username, password);
        user.setRole(role);
        userService.add(user);

        if ("ADMIN".equals(role)) {
            return "redirect:home";
        } else {
            return "redirect:la_gruta/login";
        }
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form"; // crea plantilla user-form.html para edicion
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
        return "user-form"; // la misma plantilla, pero rellenada con datos
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
