package com.training.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/*
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * 	@Configuration tags the class as a source of bean definitions for the application context.
 * 	@EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings,
 *  other beans, and various property settings.	
 * 
 * Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it 
 * 	automatically when it sees spring-webmvc on the classpath. 
 * This flags the application as a web application and activates key behaviors such as setting 
 * 	up a DispatcherServlet.
 * 	@ComponentScan tells Spring to look for other components, configurations, and services in the hello package, 
 * allowing it to find the controllers.
 */
@SpringBootApplication
public class Application {

	final static String queueName = "spring-boot";

	/*
	 * Spring AMQP requires that the Queue, the TopicExchange, and the Binding be
	 * declared as top level Spring beans in order to be set up properly.
	 */

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}
}
