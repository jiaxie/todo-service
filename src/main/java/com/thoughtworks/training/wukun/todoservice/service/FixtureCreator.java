package com.thoughtworks.training.wukun.todoservice.service;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.training.wukun.todoservice.model.Task;
import com.thoughtworks.training.wukun.todoservice.model.ToDo;
import com.thoughtworks.training.wukun.todoservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;

@Profile("!test")
@Component
public class FixtureCreator implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private ToDoService toDoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ImmutableList.of("wukun", "xiejia", "yuwei").stream()
                .filter(user -> !userService.findByUserName(user).isPresent())
                .map(user -> userService.create(User.builder().name(user).password("hello").build()))
                .forEach(user -> {
                    toDoService.create(ToDo.builder()
                            .text(String.format("finish homework, %s", user.getName()))
                            .tasks(ImmutableList.of(
                                    new Task("front-end"),
                                    new Task("back-end"),
                                    new Task("test case")
                            ))
                            .date(new Date())
                            .userId(user.getId())
                            .checked(false)
                            .build()
                    );
                });
    }
}
