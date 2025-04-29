package org.aston;

import org.aston.model.UserModel;
import org.aston.service.UserService;
import org.aston.service.UserServiceImpl;
import org.aston.util.HibernateUtil;
import org.aston.util.Validate;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl(HibernateUtil.getSessionFactory());
    private static final String ENTER_ID_USER = "Введите ID пользователя: ";
    private static final String USER_NOT_FOUND = "Пользователь не найден.";

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Создать пользователя");
            System.out.println("2. Показать пользователя");
            System.out.println("3. Обновить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Показать всех пользователей");
            System.out.println("6. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    readUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    listAllUsers();
                    break;
                case 6:
                    HibernateUtil.shutdown();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте ещё раз.");
            }
        }
    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = Main.scanner.nextLine();
        System.out.print("Введите электронную почту: ");
        String email = Main.scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = Main.scanner.nextInt();
        scanner.nextLine();

        UserModel user = new UserModel(name, email, age, LocalDateTime.now());
        Validate.validateUser(user);
        userService.create(user);
        System.out.println("Пользователь успешно создан.");
    }

    private static void readUser() {
        System.out.print(ENTER_ID_USER);
        int id = scanner.nextInt();
        scanner.nextLine();

        UserModel user = userService.read(id);
        if (user != null) {
            System.out.println("Пользователь: " + user.getName() +
                    ", Почта: " + user.getEmail() +
                    ", Возраст: " + user.getAge());
        } else {
            System.out.println(USER_NOT_FOUND);
        }
    }

    private static void updateUser() {
        try {
            System.out.print(ENTER_ID_USER);
            int id = scanner.nextInt();
            scanner.nextLine();
            UserModel existingUser = userService.read(id);

            if (existingUser == null) {
                System.out.println(USER_NOT_FOUND);
                return;
            }

            System.out.print("Введите новое имя (оставьте пустым, чтобы не изменять): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                existingUser.setName(name);
            }


            System.out.print("Введите новую почту (оставьте пустым, чтобы не изменять): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                existingUser.setEmail(email);
            }

            System.out.print("Введите новый возраст: ");
            int ageInput = scanner.nextInt();
            scanner.nextLine();
            existingUser.setAge(ageInput);

            Validate.validateUser(existingUser);
            userService.update(existingUser);
            System.out.println("Пользователь успешно обновлён.");

        } catch (InputMismatchException e) {
            System.out.println("Ошибка при вводе данных " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print(ENTER_ID_USER);
        int id = scanner.nextInt();
        scanner.nextLine();

        UserModel user = userService.read(id);
        if (user == null) {
            System.out.println(USER_NOT_FOUND);
            return;
        }

        userService.delete(id);
        System.out.println("Пользователь успешно удалён.");
    }

    private static void listAllUsers() {
        List<UserModel> users = userService.getAll();
        if (users.isEmpty()) {
            System.out.println(USER_NOT_FOUND);
        } else {
            for (UserModel user : users) {
                System.out.println("ID: " + user.getId() +
                        ", Имя: " + user.getName() +
                        ", Почта: " + user.getEmail() +
                        ", Возраст: " + user.getAge());
            }
        }
    }
}
