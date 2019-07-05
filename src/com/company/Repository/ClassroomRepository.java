package com.company.Repository;

import com.company.Domain.Classroom;
import com.company.Domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClassroomRepository {

    private static ObjectMapper objectmapper = new ObjectMapper();
    private static File file = new File("classrooms.json");

    public static void write(Classroom classroom) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            String classroomString = objectmapper.writeValueAsString(classroom);
            fileWriter.append(classroomString);
            fileWriter.append('\n');
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Classroom get(String code) {
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Classroom classroomFromFile = objectmapper.readValue(line, Classroom.class);

                if (classroomFromFile.code.equals(code)) {
                    return classroomFromFile;
                }
            }
            scanner.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static List<Classroom> getClassroomsOfUser(String username){
        List<Classroom> result = null;
        try {
            result = new ArrayList<>();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Classroom classroomFromFile = objectmapper.readValue(line, Classroom.class);

                List<User> teachers = classroomFromFile.teachers;
                if (teachers.stream().filter(tcr -> tcr.username.equals(username)).findAny().orElse(null) != null) {
                    result.add(classroomFromFile);
                }

                List<User> students = classroomFromFile.students;
                if (students.stream().filter(std -> std.username.equals(username)).findAny().orElse(null) != null) {
                    result.add(classroomFromFile);
                }
            }
            scanner.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public static void delete(String code) {
        try {
            List<String> lines = new ArrayList<String>();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Classroom classroomFromFile = objectmapper.readValue(line, Classroom.class);

                if (!classroomFromFile.code.equals(code)) {
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
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
