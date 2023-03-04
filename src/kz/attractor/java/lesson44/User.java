package kz.attractor.java.lesson44;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class User {
    private String name;
    private String surname;
    private List<Book> books;
    private String password;
    private String email;

    private String id;
    private List<Book> history;

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.books = new ArrayList<>();
        this.password = password;
        this.email = email;
        this.id = makeCode(name);
        this.history = new ArrayList<>();
    }

    public List<Book> getHistory() {
        return history;
    }

    public void setHistory(List<Book> history) {
        this.history = history;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String makeCode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return convertToString(md.digest(input.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    private String convertToString(byte[] array) {
        return IntStream.range(0, array.length / 4)
                .map(i -> array[i])
                .map(i -> (i < 0) ? i + 127 : i)
                .mapToObj(Integer::toHexString)
                .collect(Collectors.joining());
    }


}
