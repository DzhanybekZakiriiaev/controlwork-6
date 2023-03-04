package kz.attractor.java.lesson44;

import java.sql.SQLException;

public class SingleBookDataModel {
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public SingleBookDataModel(Book book){
        this.book = book;
    }
}
