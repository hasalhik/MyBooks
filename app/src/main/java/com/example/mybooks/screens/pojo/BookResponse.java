package com.example.mybooks.screens.pojo;

import java.util.List;


import com.google.gson.annotations.*;

//@TypeConverters(value = ConverterListBookString.class)
public class BookResponse {


    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("totalItems")
    @Expose
    private int totalItems;
    @SerializedName("items")
    @Expose
    private List<Book> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalBooks() {
        return totalItems;
    }

    public void setTotalBooks(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Book> getBooks() {
        return items;
    }

    public void setBooks(List<Book> items) {
        this.items = items;
    }


}
