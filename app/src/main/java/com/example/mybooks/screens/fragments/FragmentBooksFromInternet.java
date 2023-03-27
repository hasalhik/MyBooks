package com.example.mybooks.screens.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mybooks.R;
import com.example.mybooks.databinding.FragmentBooksFromInternetFragmentBinding;
import com.example.mybooks.screens.activities.MainActivity;
import com.example.mybooks.screens.adapters.BookAdapter;
import com.example.mybooks.screens.fragments.viewModels.ListBooksViewModel;
import com.example.mybooks.screens.pojo.Book;

import java.util.List;

public class FragmentBooksFromInternet extends Fragment {

    private ListBooksViewModel mViewModel;
    private FragmentBooksFromInternetFragmentBinding binding;
    private BookAdapter adapter;
    private MainActivity mainActivity;

    public FragmentBooksFromInternet(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBooksFromInternetFragmentBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(ListBooksViewModel.class);
        buildRecyclerView();
        initViewModel();
        initButtons();

    }

    private void initButtons() {
        binding.booksClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.booksTextSearch.setText("");
                binding.booksTextSearch.setVisibility(View.GONE);
                binding.booksClearSearch.setVisibility(View.GONE);
            }
        });
        binding.booksStartSearchInternet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (binding.editTextViewSearch.getText().toString().equals("")) {
                    Toast.makeText(getContext(), R.string.EntereSearchText, Toast.LENGTH_SHORT).show();
                } else {
                    hideKeyboard();
                    binding.booksClearSearch.setVisibility(View.VISIBLE);
                    binding.booksTextSearch.setText(binding.booksTextSearch.getText().toString() + binding.editTextViewSearch.getText().toString() + "; ");
                    binding.booksTextSearch.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    mViewModel.findBooks(binding.booksTextSearch.getText().toString());

                }
            }
        });
    }

    public static FragmentBooksFromInternet newInstance() {
        return new FragmentBooksFromInternet(newInstance().mainActivity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
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

            }
        });
    }

    private void initViewModel() {
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
                .create(ListBooksViewModel.class);
        mViewModel.getBookList().observe(FragmentBooksFromInternet.this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                binding.progressBar.setVisibility(View.GONE);
                adapter.setBooks(books);
                adapter.notifyDataSetChanged();
            }
        });
        mViewModel.getError().observe(FragmentBooksFromInternet.this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), throwable.getMessage() + "", Toast.LENGTH_LONG).show();
                    mViewModel.clearErrors();
                }
            }
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm  =(InputMethodManager)
                mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainActivity.getWindow().getDecorView().getWindowToken(), 0);
    }

}