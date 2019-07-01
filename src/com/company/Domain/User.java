package com.company.Domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String password;
    public List<Class> creates;
    public List<Class> joins;

    public User() {
        creates = new ArrayList<>();
        joins = new ArrayList<>();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public void AddClassToCreate(Class newClass) {
        creates.add(newClass);
    }
}

