package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Alex", "Biba", (byte)28);
        userService.saveUser("Popka", "Olelyia", (byte)23);
        userService.saveUser("Milena", "Rodster", (byte)17);
        userService.saveUser("Leroy", "Jenkins", (byte)37);

        List<User> listUserz = userService.getAllUsers();
        listUserz.forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
