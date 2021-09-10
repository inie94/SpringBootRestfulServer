package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.security.UserRepresentation;
import ru.inie.social.server.services.UserService;

import java.security.Principal;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/")
//    public String index(Model model,
//                        Principal principal){
//        User user = userService.findByEmail(principal.getName());
//        model.addAttribute("authorizedUser", user);
//        return "redirect:";
//    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRepresentation());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("user") UserRepresentation userRepresentation,
                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "register";
        if(!userRepresentation.getPassword().equals(userRepresentation.getRepeatPassword())) {
            bindingResult.rejectValue("password", "Пароли не совпадают");
            return "register";
        }

        userService.create(userRepresentation);
        return "redirect:/login";
    }
}
