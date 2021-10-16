package ru.inie.social.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.UserStatus;
import ru.inie.social.server.security.UserRepresentation;
import ru.inie.social.server.services.DTOService;
import ru.inie.social.server.services.UserService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public UserDTO getAuthorizedUser(Principal principal) {
        User user = service.findByEmail(principal.getName());
        if (user.getStatus() == null || user.getStatus().equals(UserStatus.OFFLINE)) {
            user.setStatus(UserStatus.ONLINE);
            user = service.save(user);
        }
        return DTOService.toUserDTO(user);
    }
    @PostMapping("/user/edit-profile")
    public UserDTO editUser(Principal principal,
                         @RequestBody UserDTO representation) {

        System.out.println(representation);

        if (representation.getEmail() != null && service.findByEmail(representation.getEmail()) != null) {
            /**
             *  В БД есть пользователь с таким Email.
             *  необходимо вернуть ответ в котором указывается что Email неправильный
             */
        }

        /**
         *  Записать измененные значения и сохранить в БД.
         *  Необходимо переписать метод update в классе UserService
         *  и изменить форму регистрации
         */

        User user = service.update(representation, service.findByEmail(principal.getName()));
        return DTOService.toUserDTO(user);
    }


//
//    @GetMapping("/csrf")
//    public @ResponseBody String getCsrfToken(HttpServletRequest request) {
//        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        return csrf.getToken();
//    }

}
