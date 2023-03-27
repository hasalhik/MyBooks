package com.example.mybooks.screens.api;

import com.example.mybooks.screens.pojo.BookResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


     //Запрос с параматрами
    @GET("volumes?")
    Observable<BookResponse> getBooks(@Query("q") String question,
                                              //Параметры:
                                              @Query("maxResults") int maxResult,
                                              @Query("orderBy") String orderBy,
                                              @Query("printType") String printType);
}
