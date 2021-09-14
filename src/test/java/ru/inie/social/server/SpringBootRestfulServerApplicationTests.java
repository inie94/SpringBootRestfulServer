package ru.inie.social.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class SpringBootRestfulServerApplicationTests {

	@Autowired
	private UserRepository repository;

	@Test
	@Transactional
	public void testFindAllByEmailStartsWith() {
		List<User> list = repository.findAllByEmailStartsWithIgnoreCase("jj");
	}

}
