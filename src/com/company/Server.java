package com.company;

import com.company.Command.CreateClassroomCommand;
import com.company.Command.JoinClassroomCommand;
import com.company.Domain.Classroom;
import com.company.Infrastructure.Response;
import com.company.Infrastructure.ResponseMeta;
import com.company.Domain.User;
import com.company.Repository.ClassroomRepository;
import com.company.Repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int _port;
    private ObjectMapper _objectMapper;


    public Server(int port) {
        _port = port;
        _objectMapper = new ObjectMapper();
        _objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void Run() {
        try {
            ServerSocket serverSocket = new ServerSocket(_port);

            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String requestString = dataInputStream.readUTF();

                JSONObject requestJson = new JSONObject(requestString);
                String meta = requestJson.get("meta").toString();
                String data = requestJson.get("data").toString();

                JSONObject metaJson = new JSONObject(meta);
                String messageType = metaJson.get("messageType").toString();

                switch (messageType) {
                    case "register":
                        Register(data, dataOutputStream);
                        break;
                    case "login":
                        Login(data, dataOutputStream);
                        break;
                    case "createClass":
                        CreateClassroom(data, dataOutputStream);
                        break;
                    case "joinClass":
                        JoinClassroom(data, dataOutputStream);
                }
                socket.close();
                dataInputStream.close();
                dataOutputStream.close();
            }

        } catch (Exception exception) {
            Exception ex = exception;
        }
    }

    private void Register(String data, DataOutputStream dataOutputStream) {
        try {
            String result = "fail";
            String message = "user already exist";

            User user = _objectMapper.readValue(data, User.class);
            boolean isUsernameExist = UserRepository.isUserExist(user.username);

            if (isUsernameExist == false) {
                UserRepository.write(user);

                result = "success";
                message = "user successfully registered";
            }

            ResponseMeta meta = new ResponseMeta(result, message);
            Response response = new Response(meta, user);
            String responseString = _objectMapper.writeValueAsString(response);
            dataOutputStream.writeUTF(responseString);
            dataOutputStream.flush();
        } catch (Exception exception) {
        }
    }

    private void Login(String data, DataOutputStream dataOutputStream) {
        try {
            User userFromRequest = _objectMapper.readValue(data, User.class);

            String result = "fail";
            String message = "user  did not register yet";

            boolean checkCredentials = UserRepository.checkUsernameAndPassword(userFromRequest);

            if (checkCredentials == true) {
                result = "success";
                message = "user successfully logged in";
            }

            ResponseMeta responseMeta = new ResponseMeta(result, message);
            Response response = new Response(responseMeta, userFromRequest);
            String responseString = _objectMapper.writeValueAsString(response);

            dataOutputStream.writeUTF(responseString);
            dataOutputStream.flush();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void CreateClassroom(String data, DataOutputStream dataOutputStream) throws IOException {
        CreateClassroomCommand createClassroomCommand = _objectMapper.readValue(data, CreateClassroomCommand.class);
        User teacher = UserRepository.get(createClassroomCommand.teacher);
        String randomString = Helper.getRandomAlphaNumeric(5);
        Classroom classroom = new Classroom(createClassroomCommand.name, createClassroomCommand.description, createClassroomCommand.room, randomString);
        teacher.addClassToCreates(classroom);

        UserRepository.delete(teacher.username);
        UserRepository.write(teacher);

        teacher.emptyLists();
        classroom.addTeacher(teacher);
        ClassroomRepository.write(classroom);

        String result = "success";
        String message = "Classroom successfully created";

        ResponseMeta meta = new ResponseMeta(result, message);
        Response response = new Response(meta, classroom);
        String responseString = _objectMapper.writeValueAsString(response);

        dataOutputStream.writeUTF(responseString);
        dataOutputStream.flush();
    }

    private void JoinClassroom(String data, DataOutputStream dataOutputStream) {

        try {
            //diSerial
            JoinClassroomCommand joinClassroomCommand = _objectMapper.readValue(data, JoinClassroomCommand.class);
            String code = joinClassroomCommand.code;
            String username = joinClassroomCommand.username;
            String password = joinClassroomCommand.password;

            //add student on classroom
            Classroom classroom = ClassroomRepository.get(code);
            User user = new User(username, password);
            classroom.addStudent(user);
            ClassroomRepository.delete(code);
            ClassroomRepository.write(classroom);

            //add classroom on user1
            User user1 = UserRepository.get(username);
            user.addClassToJoins(classroom);
            UserRepository.delete(username);
            UserRepository.write(user1);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
