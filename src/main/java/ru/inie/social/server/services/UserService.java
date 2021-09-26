package ru.inie.social.server.services;

import org.dom4j.util.UserDataAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inie.social.server.dto.UserDTO;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.UserStatus;
import ru.inie.social.server.repositories.UserRepository;
import ru.inie.social.server.security.UserRepresentation;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRepresentation userRepresentation) {
        User user = new User();
        user.setFirstname(userRepresentation.getFirstname());
        user.setLastname(userRepresentation.getLastname());
        user.setEmail(userRepresentation.getEmail());
        user.setGender(userRepresentation.getGender());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate;
        java.sql.Date sqlDate;

        try {
            utilDate = format.parse(userRepresentation.getDateOfBirth());
            sqlDate = new java.sql.Date(utilDate.getTime());
            user.setDateOfBirth(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setPassword(passwordEncoder.encode(userRepresentation.getPassword()));
        user.setStatus(UserStatus.OFFLINE);
        repository.save(user);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).get();
    }

    public User findById(long id) {
        return repository.findById(id).get();
    }

    public void update (UserDTO representation, User user) {
        String text;
        if ((text = representation.getFirstname()) != null) {
            user.setFirstname(text);
        }
        if ((text = representation.getLastname()) != null) {
            user.setLastname(text);
        }
        if ((text = representation.getEmail()) != null) {
            user.setEmail(text);

        }
        if ((text = representation.getGender()) != null) {
            user.setGender(text);
        }

        if (representation.getDateOfBirth() != null) {
            user.setDateOfBirth(representation.getDateOfBirth());
        }

        if ((text = representation.getPassword()) != null) {
            user.setPassword(passwordEncoder.encode(text));
        }

        repository.save(user);
    }

    public List<User> searchUsersBy(String searchValue) {
        Set<User> userSet = new HashSet<>();
        userSet.addAll(repository.findAllByEmailStartsWithIgnoreCase(searchValue));
        userSet.addAll(repository.findAllByFirstnameStartsWithIgnoreCase(searchValue));
        userSet.addAll(repository.findAllByLastnameStartsWithIgnoreCase(searchValue));
        return new ArrayList<>(userSet);
    }

}