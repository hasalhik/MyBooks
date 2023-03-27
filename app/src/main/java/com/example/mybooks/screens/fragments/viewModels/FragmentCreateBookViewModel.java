package com.example.mybooks.screens.fragments.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybooks.screens.data.AppDatabase;
import com.example.mybooks.screens.other.saveImage;
import com.example.mybooks.screens.pojo.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FragmentCreateBookViewModel extends AndroidViewModel {
    private static AppDatabase appDatabase;
    private MutableLiveData<Book> mBook;

    public FragmentCreateBookViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(getApplication());
    }


    public void insertBook(Book book) {

        if (book == null)
            Log.e("MyLog", "BOOK NULL");
        else {
            //if (book != null && appDatabase.bookDao().getBookById(book.getId()).size()==0) {
            //appDatabase.bookDao().insertBook(book);

            saveImage.imageDownload(book, getApplication());
            Log.e("MyLog", "книга добавленна" + book.getVolumeInfo().getTitle());
        }
    }
}