package com.example.mybooks.screens.fragments.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.mybooks.screens.api.ApiFactory;
import com.example.mybooks.screens.api.ApiService;
import com.example.mybooks.screens.data.AppDatabase;
import com.example.mybooks.screens.pojo.Book;
import com.example.mybooks.screens.pojo.BookResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListBooksViewModel extends AndroidViewModel {
    private MutableLiveData<List<Book>> bookList;
    private MutableLiveData<Throwable> error;
    private int MAX_RESULT = 40;
    private String ORDER_BY = "relevance";
    private String PRINT_TYPE = "books";
    private static AppDatabase appDatabase;

    public ListBooksViewModel(@NonNull Application application) {
        super(application);
        error = new MutableLiveData<>();
        appDatabase = AppDatabase.getInstance(getApplication());
    }


    private CompositeDisposable compositeDisposable;


    public void findBooks(String request) {

        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        String question = request.replace(";", "");
        Log.e("MyLog", question);

        compositeDisposable.add(apiService.getBooks(question, MAX_RESULT, ORDER_BY, PRINT_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookResponse>() {
                    @Override
                    public void accept(BookResponse bookResponse) throws Exception {
                        Log.e("MyLog", "acept");
                        Log.e("MyLog", bookList.hasActiveObservers() + "");
                        LiveData<List<Book>> MutableLive = new MutableLiveData<List<Book>>();
                        bookList.setValue(bookResponse.getBooks());
                        Log.e("MyLog", bookList.hasActiveObservers() + "");
                        for (Book b : bookResponse.getBooks())
                            Log.e("MyLog", b.getVolumeInfo().getTitle() + "");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("MyLog", "throwable");
                        error.setValue(throwable);
                    }

                })
        );
    }

    public void addBookToDb(Book book) {
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null)
            compositeDisposable.dispose();
        super.onCleared();
    }

    public void clearErrors() {
        error.setValue(null);
    }

    public MutableLiveData<List<Book>> getBookList() {
        if (bookList == null)
            bookList = new MutableLiveData<List<Book>>() {
            };
        return bookList;
    }

    public MutableLiveData<Throwable> getError() {
        return error;
    }



    public void loadBooksFromDb() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            bookList.postValue(appDatabase.bookDao().getAllBooks());
            if (bookList.getValue() == null) {Log.e("MyLog", "null:");}
            else
            Log.e("MyLog", "bookkformdb:"+ bookList.getValue().size());
            Log.e("MyLog", "bookkformdb:"+ appDatabase.bookDao().getAllBooks().size());
        });



    }
}