package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user.html";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "add-user.html";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("emailError", "Cet email existe déjà !");
            return "add-user.html";
        }

        userRepository.save(user);

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("success", "Utilisateur ajouté avec succès !");
        return "index";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalide"));

        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalide"));

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid User user,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }
}