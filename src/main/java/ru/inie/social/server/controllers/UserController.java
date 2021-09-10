package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.security.UserRepresentation;
import ru.inie.social.server.services.UserService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public User getAuthorizedUser(Principal principal) {
        return service.findByEmail(principal.getName());
    }

//    @GetMapping("/user/edit")
//    public String editPage(Principal principal,
//                           Model model) {
//
//        User authorizedUser = service.findByEmail(principal.getName());
//            model.addAttribute("user", new UserRepresentation());
//            model.addAttribute("authorizedUser", authorizedUser);
//            return "edit";
//        }
//
//        return "redirect:/id" + authorizedUser.getId();
//    }
//
//    @PostMapping("/edit-profile")
//    public String editUser(Principal principal,
//                           @ModelAttribute("user") UserRepresentation userRepresentation,
//                           BindingResult bindingResult) {
//        User authorizedUser = userService.findByEmail(principal.getName());
//
//        if (bindingResult.hasErrors())
//            return "edit";
//        if (!userRepresentation.getPassword().equals(userRepresentation.getRepeatPassword())) {
//            bindingResult.rejectValue("password", "Пароли не совпадают");
//            return "edit";
//        }
//
//        userService.update(userRepresentation, authorizedUser);
//
//        return "redirect:/feed";
//    }


//
//    @GetMapping("/csrf")
//    public @ResponseBody String getCsrfToken(HttpServletRequest request) {
//        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        return csrf.getToken();
//    }
}
