package org.example.usermanagementapplication.controller;

import jakarta.validation.Valid;
import org.example.usermanagementapplication.entity.User;
import org.example.usermanagementapplication.exception.EntityNotFoundException;
import org.example.usermanagementapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register a new user (public access)
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";  // Return to the registration page if there are errors
        }

        // Log the registration action
        System.out.println("Registering user: " + user.getUsername());

        userService.registerUser(user);

        // Redirect to the login page after successful registration
        return "redirect:/login";
    }

    // Get user by ID (available to authenticated users)
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user-details";  // Serve a user details view
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    // Get all users (ADMIN access only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";  // Serve a user list view
    }

    // Delete a user by ID (ADMIN access only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/api/users";  // Redirect after deletion
    }

    // Serve form to update a user (ADMIN access only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "update";  // Serve the update form
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    // Update user by ID (ADMIN access only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @Valid User updatedUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update";  // Return to the update form if there are errors
        }

        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(userService.encryptPassword(updatedUser.getPassword()));  // Encrypt updated password
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());  // Ensure role is updated

            userService.registerUser(user);  // Save updated user
            return "redirect:/api/users";  // Redirect to user list after successful update
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
}



