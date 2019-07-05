package com.company;

import com.company.Command.CreateClassroomCommand;
import com.company.Command.GetClassroomsOfUser;
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
import java.util.List;

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
                    case "getClassroomsOfUser":
                        getClassroomsOfUser(data, dataOutputStream);
                }
                socket.close();
                dataInputStream.close();
                dataOutputStream.close();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
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
            //Deserialize
            JoinClassroomCommand joinClassroomCommand = _objectMapper.readValue(data, JoinClassroomCommand.class);
            User userToJoin = UserRepository.get(joinClassroomCommand.username);
            String code = joinClassroomCommand.code;

            //Add student on classroom
            Classroom classroom = ClassroomRepository.get(code);
            userToJoin.emptyLists();
            classroom.addStudent(userToJoin);
            ClassroomRepository.delete(code);
            ClassroomRepository.write(classroom);

            //Add classroom on user
            classroom.emptyLists();
            userToJoin = UserRepository.get(joinClassroomCommand.username);
            userToJoin.addClassToJoins(classroom);
            UserRepository.delete(userToJoin.username);
            UserRepository.write(userToJoin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getClassroomsOfUser(String data, DataOutputStream dataOutputStream) {
        try {
            //Deserialize
            GetClassroomsOfUser getClassroomsOfUser = _objectMapper.readValue(data, GetClassroomsOfUser.class);
            List<Classroom> classrooms = ClassroomRepository.getClassroomsOfUser(getClassroomsOfUser.username);

            //Return response
            String result = "success";
            String message = "Classroom successfully retrieved";

            ResponseMeta meta = new ResponseMeta(result, message);
            Response response = new Response(meta, classrooms);
            String responseString = _objectMapper.writeValueAsString(response);

            dataOutputStream.writeUTF(responseString);
            dataOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
