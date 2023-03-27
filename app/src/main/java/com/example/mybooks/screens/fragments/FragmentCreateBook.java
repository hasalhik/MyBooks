package com.example.mybooks.screens.fragments;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.mybooks.R;
import com.example.mybooks.databinding.FragmentCreateBookFragmentBinding;
import com.example.mybooks.screens.fragments.viewModels.FragmentCreateBookViewModel;
import com.example.mybooks.screens.pojo.Book;
import com.example.mybooks.screens.pojo.BookVolumeInfo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class FragmentCreateBook extends Fragment {

    private FragmentCreateBookViewModel mViewModel;
    private FragmentCreateBookFragmentBinding binding;
    private Book book;
    ActivityResultLauncher activityResultLauncher1;
    private String bookFilePath;

    public FragmentCreateBook(Book book) {
        this.book = book;
    }

    public FragmentCreateBook() {
        book = null;

    }

    public static FragmentCreateBook newInstance() {
        return new FragmentCreateBook();
    }

    ActivityResultLauncher<String> mGetContent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        activityResultLauncher1 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d("MyLog", "cadsda " + result.getData().getExtras().get(CropImage.CROP_IMAGE_EXTRA_SOURCE));
                            Log.d("MyLog", "cadsda " + result.getData().getExtras().get(CropImage.CROP_IMAGE_EXTRA_RESULT));
                            CropImageView.CropResult result1 = (CropImageView.CropResult) result.getData().getExtras().get(CropImage.CROP_IMAGE_EXTRA_RESULT);
                            Log.d("MyLog", "cadsda " + result1.getBitmap());
                            Log.d("MyLog", "cadsda " + result1.getOriginalUri());
                            Log.d("MyLog", "cadsda " + result1.getUriFilePath(getContext(), true));
                            bookFilePath = result1.getUriFilePath(getContext(), true);
                            Log.d("MyLog", "bookFilePat "+bookFilePath);
                            File file = new File(result1.getUriFilePath(getContext(), true));
                            bookFilePath = file.getPath();
                            Log.d("MyLog", "bookFilePat "+bookFilePath);
                            binding.imageView.setImageURI(
                                    Uri.fromFile(file));


                        }
                    }
                }
        );
//         mGetContent = registerForActivityResult(new CropImageContract(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri uri) {
//                        binding.imageView.setImageURI(uri);
//                    }
//                });

        mViewModel = new ViewModelProvider(this).get(FragmentCreateBookViewModel.class);
        super.onCreate(savedInstanceState);

        binding = FragmentCreateBookFragmentBinding.inflate(getLayoutInflater());
        setInfo();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setInfo() {
        if (book != null) {

            if (book.getImageFilePath() != null) {
                Log.e("MyLog", "" + book.getImageFilePath().toString());
                File file = new File(book.getImageFilePath());
                Picasso.get().load(new File(book.getImageFilePath()))
                        .placeholder(R.drawable.default_book_cover).into(binding.imageView);
            } else
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
        } else {
            Picasso.get().load(R.drawable.default_book_cover).into(binding.imageView);

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
            String formatted = localDateTime.format(dateTimeFormatter);

            binding.textViewReleaseDate.setText(Editable.Factory.getInstance().newEditable(formatted));
        }
        binding.imageViewChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyLog", "crop");

                Log.d("MyLog", "StartAct");

                activityResultLauncher1.launch(
                        new CropImageContract().createIntent
                                (getContext(), new CropImageContractOptions
                                        (null, new CropImageOptions())));

            }


        });

        binding.imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyLog", "clk");
                if (!isFieldsEmpty()) {
                    Log.e("MyLog", "clkWork");
                    List<String> authors = new ArrayList<>();
                    authors.add(binding.textViewBookAuthor.getText().toString());

                    if (book == null) {
                        Log.e("MyLog", "BookNull");
                        BookVolumeInfo bookVolumeInfo = new BookVolumeInfo();
                        bookVolumeInfo.setTitle(binding.textViewTitle.getText().toString());
                        bookVolumeInfo.setAuthors(authors);
                        bookVolumeInfo.setDescription(binding.textViewOverview.getText().toString());
                        bookVolumeInfo.setPrintType(getString(R.string.printTypeMyBook));
                        bookVolumeInfo.setPublishedDate(binding.textViewReleaseDate.getText().toString());
                        book = new Book(null, null, null, null,
                                bookVolumeInfo);
                        if (bookFilePath != null)
                            book.setImageFilePath(bookFilePath);
                        mViewModel.insertBook(book);
                    } else {
                        Log.e("MyLog", "BookNOTNull");
                        book.getVolumeInfo().setTitle(binding.textViewTitle.getText().toString());
                        book.getVolumeInfo().setAuthors(authors);
                        book.getVolumeInfo().setPublishedDate(binding.textViewReleaseDate.getText().toString());
                        book.getVolumeInfo().setDescription(binding.textViewOverview.getText().toString());
                        if (bookFilePath != null)
                            book.setImageFilePath(bookFilePath);
                        mViewModel.insertBook(book);


                    }
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new FragmentListMyBooks()).commit();

                }
            }
        });

    }

    private boolean isFieldsEmpty() {
        boolean result = false;
        if (binding.textViewTitle.getText().toString().equals("")) {
            binding.textViewTitle.setError(getString(R.string.addValue));
            result = true;
        }
        if (binding.textViewBookAuthor.getText().toString().equals("")) {
            binding.textViewBookAuthor.setError(getString(R.string.addValue));
            result = true;
        }
        if (binding.textViewOverview.getText().toString().equals("")) {
            binding.textViewOverview.setError(getString(R.string.addValue));
            result = true;
        }
        if (binding.textViewReleaseDate.getText().toString().equals("")) {
            binding.textViewReleaseDate.setError(getString(R.string.addValue));
            result = true;
        }

        return result;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }


}