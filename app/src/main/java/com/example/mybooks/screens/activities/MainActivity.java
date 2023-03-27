package com.example.mybooks.screens.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mybooks.R;
import com.example.mybooks.databinding.ActivityMainBinding;
import com.example.mybooks.screens.data.AppDatabase;
import com.example.mybooks.screens.data.BookDao;
import com.example.mybooks.screens.fragments.FragmentBookDetail;
import com.example.mybooks.screens.fragments.FragmentBooksFromInternet;
import com.example.mybooks.screens.fragments.FragmentCreateBook;
import com.example.mybooks.screens.fragments.FragmentListMyBooks;
import com.example.mybooks.screens.other.saveImage;
import com.example.mybooks.screens.pojo.Book;
import com.google.android.material.navigation.NavigationBarView;

public  class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final Object LOCK = new Object();
    public MainActivity getInstance(){
        synchronized (LOCK) {
            return MainActivity.this;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideActionBar();
        setButtonMenu();
        saveImage.verifyStoragePermissions(this);
    }


    private void hideActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }


    private void setButtonMenu() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentListMyBooks()).commit();
        binding.btNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentListMyBooks()).commit();
                        break;
                    case R.id.addNewBook:
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentCreateBook()).commit();
                        break;
                    case R.id.addForInternet:
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentBooksFromInternet(MainActivity.this)).commit();
                        break;

                }
                return true;
            }
        });

    }

    public void replaceFragmentToDetail(Book book) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).replace(R.id.placeHolder, new FragmentBookDetail(book));
    }
}