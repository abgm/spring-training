package com.training.rabbitmq;
/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.rabbitmq.Application;
import com.training.rabbitmq.Receiver;
import com.training.rabbitmq.SenderRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	private final List<String> messageList = Arrays.asList("Hello from RabbitMQ!", "Hello from RabbitMQ!1",
			"Hello from RabbitMQ!2", "Hello from RabbitMQ!3", "Hello from RabbitMQ!4", "Hello from RabbitMQ!5");

	@MockBean
	SenderRunner senderRunner;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Receiver receiver;

	private CountDownLatch latch;

	@Before
	public void setUp() {
		latch = new CountDownLatch(messageList.size());
		receiver.setLatch(latch);
		receiver.getMessages().clear();
	}

	@Test
	public void sendParallelStream() throws Exception {
		messageList.parallelStream().forEach(s -> rabbitTemplate.convertAndSend(Application.queueName, s));
		latch.await(10000, TimeUnit.MILLISECONDS);
		assertTrue(messageList.size() == receiver.getMessages().size());
	}

	@Test
	public void sendStream() throws Exception {
		messageList.stream().forEach(s -> rabbitTemplate.convertAndSend(Application.queueName, s));
		latch.await(10000, TimeUnit.MILLISECONDS);
		assertTrue(messageList.equals(receiver.getMessages()));
	}

}
