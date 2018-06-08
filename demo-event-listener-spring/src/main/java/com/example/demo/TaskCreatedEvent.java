package com.example.demo;

import org.springframework.context.ApplicationEvent;

public class TaskCreatedEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5191249496697425773L;

	public TaskCreatedEvent(Task task) {
		super(task);
	}

}
