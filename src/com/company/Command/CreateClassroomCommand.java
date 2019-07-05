package com.company.Command;

public class CreateClassroomCommand {
    public String teacher;
    public String name;
    public String description;
    public String room;

    private CreateClassroomCommand() {
    }

    public CreateClassroomCommand(String teacher, String name, String description, String room) {
        this.teacher = teacher;
        this.name = name;
        this.description = description;
        this.room = room;
    }
}

