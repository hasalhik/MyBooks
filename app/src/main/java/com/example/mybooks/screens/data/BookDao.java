package com.example.mybooks.screens.data;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mybooks.screens.pojo.Book;

import java.lang.invoke.MutableCallSite;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM book")
    List<Book> getAllBooks();

    @Query("SELECT * FROM book  WHERE id = :nId")
    List<Book> getBookById(String nId);

    @Query("SELECT * FROM book WHERE localId = :nLocalId ")
    List<Book> getBookByLocalId(int nLocalId);

    @Query("DELETE FROM book")
    void deleteAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(List<Book> employeeList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book book);

    @Delete
    void deleteBook(Book book);

}
