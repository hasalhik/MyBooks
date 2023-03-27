package com.example.mybooks.screens.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mybooks.R;
import com.example.mybooks.databinding.FragmentBookDetailFragmentBinding;
import com.example.mybooks.screens.fragments.viewModels.FragmentBookDetailViewModel;
import com.example.mybooks.screens.pojo.Book;
import com.squareup.picasso.Picasso;

import java.io.File;

public class FragmentBookDetail extends Fragment {

    private FragmentBookDetailViewModel mViewModel;
    private FragmentBookDetailFragmentBinding binding;
    private Book book;
    private Book favoriteBook = null;

    public FragmentBookDetail(Book book) {
        this.book = book;
    }

    public static FragmentBookDetail newInstance() {
        return new FragmentBookDetail(newInstance().book);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBookDetailFragmentBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(FragmentBookDetailViewModel.class);
        setContent();
    }

    private void setContent() {
        if (book.getImageFilePath()!= null){
            Log.e("MyLog", "" + book.getImageFilePath().toString());
            File file = new File(book.getImageFilePath());
            Picasso.get().load(new File(book.getImageFilePath()))
                    .placeholder(R.drawable.default_book_cover).into(binding.imageView);
        }
        else
            Picasso.get().load("https://books.google.com/books/content?id=" + book.getId() + "&printsec=frontcover&img=2&zoom=10&edge=curl&source=gbs_api")
                    .placeholder(R.drawable.default_book_cover).into(binding.imageView);

        binding.textViewTitle.setText(book.getVolumeInfo().getTitle());
        if (book.getVolumeInfo().getAuthors() != null)
            binding.textViewBookAuthor.setText(book.getVolumeInfo().getAuthors().toString()
                    .replace("[", "")
                    .replace("]", ""));
        if (book.getVolumeInfo().getPublishedDate() != null)
            binding.textViewReleaseDate.setText(book.getVolumeInfo().getPublishedDate());
        if (book.getVolumeInfo().getDescription() != null)
            binding.textViewOverview.setText(book.getVolumeInfo().getDescription());


        mViewModel.setBook(book);

        mViewModel.getBook().observe(FragmentBookDetail.this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book != null) favorToOn();
                else favorToOff();
                favoriteBook = book;
            }
        });
        binding.imageViewAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteBook != null) {
                    deleteFavoriteBook(book);
                } else {
                    favorToOn();
                    mViewModel.insertBook(book);
                    Toast.makeText(FragmentBookDetail.this.getContext(), R.string.BookInsert, Toast.LENGTH_SHORT);
                }

            }
        });
        binding.imageViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentCreateBook(book)).commit();
            }
        });


    }

    private void deleteFavoriteBook(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.areYouSureDelete)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mViewModel.deleteBook(book);
                        favoriteBook = null;
                        Toast.makeText(FragmentBookDetail.this.getContext(), R.string.BookRemved, Toast.LENGTH_SHORT);
                        getParentFragmentManager().popBackStack();
                        favorToOff();
                    }
                })
                .setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void favorToOn() {
        binding.imageViewAddToFavourites.setColorFilter(getContext().getResources().getColor(R.color.read));

    }

    private void favorToOff() {
        binding.imageViewAddToFavourites.setColorFilter(getContext().getResources().getColor(R.color.grey));
    }

}