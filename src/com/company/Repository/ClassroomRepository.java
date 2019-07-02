package com.company.Repository;

import com.company.Domain.Classroom;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassroomRepository {

    private static ObjectMapper objectmapper = new ObjectMapper();
    private static File file = new File("classrooms.json");

    public static void Write(Classroom classroom) {
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
}
