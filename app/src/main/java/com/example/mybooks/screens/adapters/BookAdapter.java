package com.example.mybooks.screens.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybooks.R;
import com.example.mybooks.databinding.BookItemBinding;
import com.example.mybooks.screens.fragments.viewModels.ListBooksViewModel;
import com.example.mybooks.screens.pojo.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private ListBooksViewModel mViewMode;
    private OnBookClickListener onBookClickListener;


    public interface OnBookClickListener {
        void onPosterClick(Book boo, int position);
    }

    public void setOnBookClickListener(BookAdapter.OnBookClickListener onBookClickListener) {
        this.onBookClickListener = onBookClickListener;
    }

    public BookAdapter(ListBooksViewModel mViewMode) {
        this.mViewMode = mViewMode;
        books = new ArrayList<Book>() {
        };
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        BookItemBinding binding;

        public BookViewHolder(View itemView) {
            super(itemView);
            binding = BookItemBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mViewMode.insertBook(books.get(getAdapterPosition()));
                    if (onBookClickListener != null) {
                        onBookClickListener.onPosterClick(books.get(getAdapterPosition()), getAdapterPosition());
                    }
                    Log.e("MyLog", "onclic");

                }
            });
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.binding.textViewBookName.setText(book.getVolumeInfo().getTitle());
        if (book.getVolumeInfo().getAuthors() != null)
            holder.binding.textViewBookAuthor.setText(book.getVolumeInfo().getAuthors().toString()
                    .replace("[", "")
                    .replace("]", ""));
        else holder.binding.textViewBookAuthor.setText(R.string.noAuthor);

        if (book.getImageFilePath() != null) {
            Log.e("MyLog", "" + book.getImageFilePath().toString());
            File file = new File(book.getImageFilePath());
            Picasso.get().load(new File(book.getImageFilePath()))
                    .placeholder(R.drawable.default_book_cover).into(holder.binding.bookItemImage);
        } else
            Picasso.get().load("https://books.google.com/books/content?id=" + book.getId() + "&printsec=frontcover&img=2&zoom=10&edge=curl&source=gbs_api")
                    .placeholder(R.drawable.default_book_cover).into(holder.binding.bookItemImage);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
