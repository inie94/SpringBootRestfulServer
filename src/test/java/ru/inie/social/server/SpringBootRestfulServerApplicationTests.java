package ru.inie.social.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inie.social.server.entities.User;
import ru.inie.social.server.entities.enums.TopicStatus;
import ru.inie.social.server.repositories.UserRepository;
import ru.inie.social.server.services.TopicService;
import ru.inie.social.server.services.UserService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class SpringBootRestfulServerApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private TopicService topicService;

	@Test
	@Transactional
	public void find() {
		System.out.println("kjhgjhgkhg");
		User subscriber = userService.findById(3);
		User unsubscribe = userService.findById(2);
		System.out.println(topicService.getTopicBySubscriberAndUnsubscribe(subscriber, unsubscribe, TopicStatus.PRIVATE).getId() + " topic  is found");;

	}

	@Test
	public void test() {

		List<String> list1= new LinkedList<>(Arrays.asList("FIRST", "SECOND", "THIRD"));
		List<String> list2= new LinkedList<>(Arrays.asList("SECOND", "FIVES"));

		list1.removeAll(list2);

		System.out.println(list1);
	}
}
