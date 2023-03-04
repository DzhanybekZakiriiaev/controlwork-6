package kz.attractor.java;

import kz.attractor.java.lesson44.Lesson47Server;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson47Server("localhost", 9999).start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
