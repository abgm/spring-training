package com.example.demo;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class TaskCreatedEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TaskCreatedEventListener.class);

    @Resource
    EntityManager entityManager;

    //@Async
    @EventListener
   // @TransactionalEventListener
    public void handleEvent(TaskCreatedEvent taskCreatedEvent) throws InterruptedException {
        Task task = (Task)taskCreatedEvent.getSource();
        LOG.info("Is task transient? {}", isTransient(task));
        LOG.info("Is task managed? {}", isManaged(task));
        LOG.info("Is task detached? {}", isDetached(task));
    }

    private boolean isTransient(Task task) {
        return task.getId() == null;
    }

    private boolean isManaged(Task task) {
        return entityManager.contains(task);
    }

    private boolean isDetached(Task task) {
        return !isTransient(task)
            && !isManaged(task)
            && exists(task);
    }

    private boolean exists(Task task) {
        return entityManager.find(Task.class, task.getId()) != null;
    }

}