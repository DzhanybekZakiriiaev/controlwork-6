package kz.attractor.java.lesson44;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static List<Day> readDaysFile() {
        String json = "";
        List<Day> days = new ArrayList<>();
        try{
            Path path = Paths.get("base/Days.json");
            json = Files.readString(path);
            for(int i = 0; i < GSON.fromJson(json, Day[].class).length;i++){
                days.add(GSON.fromJson(json, Day[].class)[i]);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return days;
    }
    public static void  writeDaysFile(List<Day> days){
        String json = GSON.toJson(days);
        try{
            Path path = Paths.get("base/Days.json");
            Files.write(path, json.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<Book> readBookFile() {
        String json = "";
        List<Book> books = new ArrayList<>();
        try{
            Path path = Paths.get("base/Books.json");
            json = Files.readString(path);
            for(int i = 0; i < GSON.fromJson(json, Book[].class).length;i++){
                books.add(GSON.fromJson(json, Book[].class)[i]);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return books;
    }
    public static List<User> readUserFile() {
        String json = "";
        List<User> users = new ArrayList<>();
        try{
            Path path = Paths.get("base/Users.json");
            json = Files.readString(path);
            for(int i = 0; i < GSON.fromJson(json, User[].class).length;i++){
                users.add(GSON.fromJson(json, User[].class)[i]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
    public static void addUser(List<String> stats){
        List<User> users = readUserFile();
        User newUser = new User(stats.get(0), stats.get(1), stats.get(2), stats.get(3));
        users.add(newUser);
        String json = GSON.toJson(users);
        try{
            Path path = Paths.get("base/Users.json");
            Files.write(path, json.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void  writeUserFile(List<User> users){
        String json = GSON.toJson(users);
        try{
            Path path = Paths.get("base/Users.json");
            Files.write(path, json.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void  writeBookFile(List<Book> books){
        String json = GSON.toJson(books);
        try{
            Path path = Paths.get("base/Books.json");
            Files.write(path, json.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
