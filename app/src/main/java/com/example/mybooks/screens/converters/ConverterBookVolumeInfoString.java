package com.example.mybooks.screens.converters;

import androidx.room.TypeConverter;

import com.example.mybooks.screens.pojo.Book;
import com.example.mybooks.screens.pojo.BookVolumeInfo;
import com.google.gson.Gson;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConverterBookVolumeInfoString {
    @TypeConverter
    public static String bookVolumeInfoToString(BookVolumeInfo bookVolumeInfo) {
        return new Gson().toJson(bookVolumeInfo);

    }
    @TypeConverter
    public BookVolumeInfo stringToBookVolumeInfo(String bookVolumeInfoAsString) {

        return new Gson().fromJson(bookVolumeInfoAsString, BookVolumeInfo.class);

    }
    @TypeConverter
    public static String fileToString(File file) {
        return new Gson().toJson(file);

    }
    @TypeConverter
    public static File stringToFile(String string) {
        return new Gson().fromJson(string, File.class);

    }
}