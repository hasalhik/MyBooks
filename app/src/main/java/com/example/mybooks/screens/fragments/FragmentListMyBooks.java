package com.example.mybooks.screens.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mybooks.R;
import com.example.mybooks.databinding.FragmentListMyBooksFragmentBinding;
import com.example.mybooks.screens.activities.MainActivity;
import com.example.mybooks.screens.adapters.BookAdapter;
import com.example.mybooks.screens.fragments.viewModels.ListBooksViewModel;
import com.example.mybooks.screens.pojo.Book;

import java.util.List;

public class FragmentListMyBooks extends Fragment {

    private ListBooksViewModel mViewModel;
    private FragmentListMyBooksFragmentBinding binding;
    private BookAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentListMyBooksFragmentBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(ListBooksViewModel.class);
        buildRecyclerView();
        initViewModel();

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadBooksFromDb();
        Log.e("MyLog", "onResum");
    }

    private void buildRecyclerView() {
        adapter = new BookAdapter(mViewModel);
        binding.booksRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        binding.booksRecycleView.setAdapter(adapter);
        adapter.setOnBookClickListener(new BookAdapter.OnBookClickListener() {
            @Override
            public void onPosterClick(Book book, int position) {

                ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).replace(R.id.placeHolder, new FragmentBookDetail(book)).commit();
                Log.e("MyLog", "onBookClic");


            }
        });
    }

    private void initViewModel() {
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(ListBooksViewModel.class);
        mViewModel.getBookList().observe(FragmentListMyBooks.this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                Log.e("MyLog", "onChenge:" + books.size());
                adapter.setBooks(books);
                adapter.notifyDataSetChanged();
            }
        });
        mViewModel.getError().observe(FragmentListMyBooks.this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null) {
                    Toast.makeText(getContext(), throwable.getMessage() + "", Toast.LENGTH_LONG).show();
                    mViewModel.clearErrors();
                }
            }
        });
        mViewModel.loadBooksFromDb();
    }

    public static FragmentListMyBooks newInstance() {
        return new FragmentListMyBooks();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return binding.getRoot();

    }


}