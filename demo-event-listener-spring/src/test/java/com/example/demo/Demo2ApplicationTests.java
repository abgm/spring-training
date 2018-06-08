package com.example.demo;

import static org.mockito.Mockito.after;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo2ApplicationTests {

	@Autowired
	TaskService taskService;
	@Test
	public void contextLoads() {
		 taskService.createTask("abel");
	}

}
