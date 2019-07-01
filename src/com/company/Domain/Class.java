package com.company.Domain;

import java.util.ArrayList;
import java.util.List;

public class Class {
    public String name;
    public String description;
    public String room;
    public String code;

    public List<User> techers;
    public List<User> students;

    public Class() {
        techers = new ArrayList<>();
        students = new ArrayList<>();
    }

    public Class(String name, String description, String room, String code) {
        this();
        this.name = name;
        this.description = description;
        this.room = room;
        this.code = code;
    }

    public void AddTeacher(User teacher) {
        techers.add(teacher);
    }

    public void AddStudent(User student) {
        students.add(student);
    }
}
