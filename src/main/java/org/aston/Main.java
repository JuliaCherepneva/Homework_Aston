package org.aston;

import org.aston.model.UserModel;
import org.aston.service.ServiceImpl;
import org.aston.service.UserService;
import org.aston.util.HibernateUtil;
import org.aston.util.Validate;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServiceImpl userService = new UserService(HibernateUtil.getSessionFactory());
        Scanner scanner = new Scanner(System.in);

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
                    createUser(userService, scanner);
                    break;
                case 2:
                    readUser(userService, scanner);
                    break;
                case 3:
                    updateUser(userService, scanner);
                    break;
                case 4:
                    deleteUser(userService, scanner);
                    break;
                case 5:
                    listAllUsers(userService);
                    break;
                case 6:
                    HibernateUtil.shutdown();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте ещё раз.");
            }
        }
    }

    private static void createUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите электронную почту: ");
        String email = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        UserModel user = new UserModel(name, email, age, LocalDateTime.now());
        Validate.validateUser(user);
        // немного переписал код с учётом метода валидации
        userService.create(user);
        System.out.println("Пользователь успешно создан.");
    }

    private static void readUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Введите ID пользователя: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        UserModel user = userService.read(id);
        if (user != null) {
            System.out.println("Пользователь: " + user.getName() +
                    ", Почта: " + user.getEmail() +
                    ", Возраст: " + user.getAge());
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private static void updateUser(ServiceImpl userService, Scanner scanner) {
        try {
            System.out.print("Введите ID пользователя: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            UserModel existingUser = userService.read(id);

            if (existingUser == null) {
                System.out.println("Пользователь не найден.");
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
            // здесь тоже чуть-чуть переписал целый метод updateUser,
            // можно откатиться если такой вариант не валиден
            userService.update(existingUser);
            System.out.println("Пользователь успешно обновлён.");

        } catch (InputMismatchException e) {
            System.out.println("Ошибка при вводе данных " + e.getMessage());
        }
    }

    private static void deleteUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Введите ID пользователя: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        UserModel user = userService.read(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        userService.delete(id);
        System.out.println("Пользователь успешно удалён.");
    }

    private static void listAllUsers(ServiceImpl userService) {
        List<UserModel> users = userService.getAll();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
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
