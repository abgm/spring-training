package com.example;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

/**
 * <p>
 * The Receiver is a simple POJO that defines a method for receiving messages.
 * When you register it to receive messages, you can name it anything you want.
 * </p>
 * 
 * @author abgm
 */

@Component
public class Receiver {
	private List<String> messages = new LinkedList<String>();
	private CountDownLatch latch;

	public void receiveMessage(String message) {
		messages.add(message);

		if (latch != null) {
			latch.countDown();
		}
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

}
