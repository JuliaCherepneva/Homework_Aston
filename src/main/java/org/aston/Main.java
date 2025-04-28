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
            System.out.println("1. Create User");
            System.out.println("2. Read User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        UserModel user = new UserModel(name, email, age, LocalDateTime.now());
        Validate.validateUser(user);
        // немного переписал код с учётом метода валидации
        userService.create(user);
        System.out.println("User created successfully.");
    }

    private static void readUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Enter user ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        UserModel user = userService.read(id);
        if (user != null) {
            System.out.println("User: " + user.getName() + ", Email: " + user.getEmail() + ", Age: " + user.getAge());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void updateUser(ServiceImpl userService, Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            UserModel existingUser = userService.read(id);

            if (existingUser == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter new name: ");
            String name = scanner.nextLine();
            existingUser.setName(name);

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();
            existingUser.setEmail(email);

            System.out.print("Enter new age: ");
            int ageInput = scanner.nextInt();
            scanner.nextLine();
            existingUser.setAge(ageInput);

            Validate.validateUser(existingUser);
            // здесь тоже чуть-чуть переписал целый метод updateUser,
            // можно откатиться если такой вариант не валиден
            userService.update(existingUser);
            System.out.println("User updated successfully.");

        } catch (InputMismatchException e) {
            System.out.println("Ошибка при вводе данных " + e.getMessage());
        }
    }

    private static void deleteUser(ServiceImpl userService, Scanner scanner) {
        System.out.print("Enter user ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        UserModel user = userService.read(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        userService.delete(id);
        System.out.println("User deleted successfully.");
    }

    private static void listAllUsers(ServiceImpl userService) {
        List<UserModel> users = userService.getAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (UserModel user : users) {
                System.out.println("ID: " + user.getId() + ", Name: " + user.getName() +
                        ", Email: " + user.getEmail() + ", Age: " + user.getAge());
            }
        }
    }
}