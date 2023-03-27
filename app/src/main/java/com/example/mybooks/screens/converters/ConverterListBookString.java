package com.example.mybooks.screens.converters;

import androidx.room.TypeConverter;

import com.example.mybooks.screens.pojo.Book;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ConverterListBookString {
    @TypeConverter
    public static String listBookToString(List<Book> books) {
        return new Gson().toJson(books);

    }
    @TypeConverter
    public List<Book> stringToListBook(String bookAsString) {
        Gson gson = new Gson();
        ArrayList object = gson.fromJson(bookAsString, ArrayList.class);
        ArrayList<Book> books = new ArrayList<>();
        for (Object o : object)
            books.add(gson.fromJson(o.toString(), Book.class));
        return books;

    }
}