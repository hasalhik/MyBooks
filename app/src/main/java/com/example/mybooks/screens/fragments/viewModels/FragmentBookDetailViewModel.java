package com.example.mybooks.screens.fragments.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mybooks.screens.activities.MainActivity;
import com.example.mybooks.screens.other.saveImage;
import com.example.mybooks.screens.data.AppDatabase;
import com.example.mybooks.screens.pojo.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentBookDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Book> mBook;
    private MutableLiveData<Throwable> error;
    private static AppDatabase appDatabase;

    public FragmentBookDetailViewModel(@NonNull Application application) {
        super(application);
        error = new MutableLiveData<>();
        appDatabase = AppDatabase.getInstance(getApplication());
    }

    public void setBook(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Book> bookList = null;
            if (book != null) {
                if (book.getId() != null)
                    bookList = appDatabase.bookDao().getBookById(book.getId());
                else
                    bookList = appDatabase.bookDao().getBookByLocalId(book.getLocalId());
            }
            if (bookList != null)
                if (bookList.size() == 1)
                    this.mBook.postValue(bookList.get(0));
                else Log.e("MyLog", "Найдено несколько книг");
            if (bookList.size() == 0)
                Log.e("MyLog", "Найдено 0 книг");
        });
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

    public void deleteBook(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (book != null)
                appDatabase.bookDao().deleteBook(book);
            Log.e("MyLog", "книга удаленна" + book.getVolumeInfo().getTitle());
        });
    }


    public MutableLiveData<Book> getBook() {
        if (mBook == null)
            mBook = new MutableLiveData<Book>() {
            };
        return mBook;
    }
}