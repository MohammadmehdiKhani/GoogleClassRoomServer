package com.company;

import com.company.Domain.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        try {
//            File file = new File("Hello.json");
//
//            Scanner scanner = new Scanner(file);
//
//
//
//            boolean rs = file.createNewFile();
//            FileWriter fw = new FileWriter(file);
//            fw.write("Hello.json");
//            fw.flush();
//
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String le = line;
//            }
//
//            fw.close();
//            scanner.close();
//
//            boolean rss = file.delete();
//            String rx = "he";
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Server server = new Server(6666);
        server.Run();
    }
}

