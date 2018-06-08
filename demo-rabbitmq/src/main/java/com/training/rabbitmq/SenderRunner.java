package com.training.rabbitmq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SenderRunner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;
	private final ConfigurableApplicationContext context;

	public SenderRunner(Receiver receiver, RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
		this.context = context;
	}

	@Override
	public void run(String... args) throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		receiver.setLatch(latch);

		System.out.println("Sending message...");
		rabbitTemplate.convertAndSend(Application.queueName, "Hello from RabbitMQ!");

		latch.await(10000, TimeUnit.MILLISECONDS);
		receiver.getMessages().stream().map(s -> ("Receber <" + s + ">")).forEach(System.out::println);

		context.close();
	}

}