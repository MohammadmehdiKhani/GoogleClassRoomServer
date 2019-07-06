package com.company.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class AssignmentRepository {

    private static ObjectMapper objectmapper = new ObjectMapper();
    private static File file = new File("assignments.json");
}
