package com.example.ad2;

import java.util.ArrayList;

public class GetRecommendItem {
    private String title;
    private String author;
    private String isbn;
    private ArrayList<String> number;

    public GetRecommendItem(ArrayList<String> number){
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    public void setNumber(ArrayList<String> number) {
        this.number = number;
    }
}
