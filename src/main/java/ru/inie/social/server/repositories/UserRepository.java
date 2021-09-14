package ru.inie.social.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAllByEmailStartsWithIgnoreCase(String searchValue);
    List<User> findAllByFirstnameStartsWithIgnoreCase(String searchValue);
    List<User> findAllByLastnameStartsWithIgnoreCase(String searchValue);

//    List<User> findAllByEmailOrFirstnameOrLastnameIsStartingWith(String searchValue);

}
