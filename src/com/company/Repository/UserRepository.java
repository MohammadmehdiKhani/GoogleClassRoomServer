package com.company.Repository;

import com.company.Domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository {

    private static ObjectMapper objectmapper = new ObjectMapper();
    private static File file = new File("users.json");

    public static void write(User user) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String userString = objectmapper.writeValueAsString(user);
            fileWriter.append(userString);
            fileWriter.append('\n');
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean checkUsernameAndPassword(User user) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                User userFromFile = objectmapper.readValue(line, User.class);

                if (userFromFile.username.equals(user.username)
                        && userFromFile.password.equals(user.password)) {
                    return true;
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isUserExist(String username) {
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                User userFromFile = objectmapper.readValue(line, User.class);

                if (userFromFile.username.equals(username)) {
                    return true;
                }
            }
            scanner.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static User get(String username) {
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                User userFromFile = objectmapper.readValue(line, User.class);

                if (userFromFile.username.equals(username)) {
                    return userFromFile;
                }
            }
            scanner.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void delete(String username) {
        try {
            List<String> lines = new ArrayList<String>();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                User userFromFile = objectmapper.readValue(line, User.class);

                if (!userFromFile.username.equals(username)) {
                    lines.add(line);
                }
            }

            scanner.close();
            delete();
            FileWriter fileWriter = new FileWriter(file, true);

            for (String line :
                    lines) {
                fileWriter.append(line);
                fileWriter.append('\n');
                fileWriter.flush();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void delete() {
        try {
            File file = new File("users.json");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
