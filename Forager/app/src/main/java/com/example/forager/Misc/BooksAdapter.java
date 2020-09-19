package com.example.forager.Misc;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.forager.Activities.MainActivity;
import com.example.forager.Activities.SelectedBookActivity;
import com.example.forager.Database.Book;
import com.example.forager.R;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private List<Book> books;
    private Context context;
    private final LayoutInflater inflater;

    // constructor for BookListActivity
    public BooksAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    // constructor for BookResultsActivity
    public BooksAdapter(Context context, List<Book> bookData){
        this.books = bookData;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new ViewHolder(context, inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BooksAdapter.ViewHolder holder, int position){
        if (books != null)
            holder.bindTo(books.get(position));
         else {
            // Covers the case of data not being ready yet.
        }
    }

    @Override
    public int getItemCount(){
        if(books != null)
            return books.size();
        return 0;
    }

    public void setBooks(List<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }

    public Book getBookAtPosition(int position){
        return books.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView titleTextView;
        private TextView authorsTextView;
        private TextView descriptionTextView;
        private ImageView bookImage;
        private Context context;

        ViewHolder(Context context, View itemView){
            super(itemView);

            titleTextView = itemView.findViewById(R.id.bookTitle);

            authorsTextView = itemView.findViewById(R.id.authorsTitleItem);

            descriptionTextView = itemView.findViewById(R.id.bookDescriptionItem);

            bookImage = itemView.findViewById(R.id.bookImage);

            this.context = context;

            itemView.setOnClickListener(this);
        }

        void bindTo(Book currentBook){
            titleTextView.setText(currentBook.getTitle());

            authorsTextView.setText(currentBook.getAuthors());

            descriptionTextView.setText(currentBook.getDescription());

            Glide.with(context)
                    .load(currentBook.getThumbnail())
                    .centerCrop()
                    .into(bookImage);
        }

        @Override
        public void onClick(View view) {
            Book currentBook = books.get(getAdapterPosition());

            Intent selectedBookIntent = new Intent(context, SelectedBookActivity.class);

            selectedBookIntent.putExtra("selectedBook",  currentBook);

            String intentGreeting = "You just came from the " + context.getClass().getSimpleName() + " page";

            selectedBookIntent.putExtra(MainActivity.GREETING, intentGreeting);

            ((Activity) context).startActivityForResult(selectedBookIntent, MainActivity.SELECTED_BOOK_REQUEST);
        }

    }
}
