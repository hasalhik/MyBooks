package com.example.mybooks.screens.other;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.mybooks.screens.data.AppDatabase;
import com.example.mybooks.screens.pojo.Book;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class saveImage {
    private static AppDatabase appDatabase;

    public static void imageDownload(Book book, Context context) {


        Picasso.get().load("https://books.google.com/books/content?id=" +
                book.getId() + "&printsec=frontcover&img=2&zoom=10&edge=curl&source=gbs_api")
                .into(getTarget(book, context));

    }

    //target to save
    private static Target getTarget(Book book, Context context) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            Log.e("Mulog","0");
                            File file = new File(context.getFilesDir().getPath(),book.getId()+".jpg");

                            //file.createNewFile();
                            Log.e("Mulog","1");
                            FileOutputStream ostream = new FileOutputStream(file);
                            Log.e("Mulog","2");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            Log.e("Mulog","3");
                            ostream.flush();
                            Log.e("Mulog","4");
                            ostream.close();
                            book.setImageFilePath(file.getPath());
                            appDatabase = AppDatabase.getInstance(context);
                            appDatabase.bookDao().insertBook(book);


                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                            Log.e("IOException", e.getMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        };
        return target;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
