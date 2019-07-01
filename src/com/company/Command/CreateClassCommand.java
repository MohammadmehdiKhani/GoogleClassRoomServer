package com.company.Command;

public class CreateClassCommand {
    public String teacher;
    public String name;
    public String description;
    public String room;
    public String uniqueCode;


    private CreateClassCommand() {
    }

    public CreateClassCommand(String teacher, String name, String description, String room) {
        this.teacher = teacher;
        this.name = name;
        this.description = description;
        this.room = room;
    }

    public void setUniqueCode(String code)
    {
        this.uniqueCode = code;
    }
}
