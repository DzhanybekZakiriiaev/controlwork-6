package kz.attractor.java.lesson44;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDataModel {
    private List<Book> books = new ArrayList<>();

    public BookDataModel() {
        this.books.addAll(FileService.readBookFile());
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
