package ru.inie.social.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.LinkedList;
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

	@Test
	public void test() {

		List<String> list1= new LinkedList<>(Arrays.asList("FIRST", "SECOND", "THIRD"));
		List<String> list2= new LinkedList<>(Arrays.asList("SECOND", "FIVES"));

		list1.removeAll(list2);

		System.out.println(list1);
	}
}
