package com.example.demo;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Task createTask(String name) {

        Task task = new Task();
        task.setName(name);
        task.setCreated(LocalDateTime.now());

        LOG.info("Publishing task created event: {}", task);

        eventPublisher.publishEvent(new TaskCreatedEvent(task));

        try {
        	try {
        	getClass().getClassLoader().getResource("teste");
        	}catch (Exception e) {
				// TODO: handle exception
			}
        	return taskRepository.save(task);
        } finally {
            LOG.info("Event published. Saving task: {}", task);
        }
    }
}