package com.example.forager.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forager.Misc.BooksAdapter;
import com.example.forager.Database.Book;
import com.example.forager.Database.BookViewModel;
import com.example.forager.R;

import java.util.List;

public class BookListActivity extends AppCompatActivity {
    public static final String REPLY_KEY = "BOOK_LIST_REPLY";
    private BookViewModel bookViewModel;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toast.makeText(getApplicationContext(), getIntent().getStringExtra(MainActivity.GREETING), Toast.LENGTH_SHORT).show();

        progress = findViewById(R.id.bookListProgressBar);

        Intent reply_intent = new Intent();

        reply_intent.putExtra(REPLY_KEY, getString(R.string.book_list_reply));

        setResult(RESULT_OK, reply_intent);

        RecyclerView recyclerView = findViewById(R.id.bookListRecyclerView);

        final BooksAdapter adapter = new BooksAdapter(this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable final List<Book> books) {
                progress.setVisibility(View.INVISIBLE);
                // Update the cached copy of the words in the adapter.
                adapter.setBooks(books);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        Book swipedBook  = adapter.getBookAtPosition(position);

                        Toast.makeText(getApplicationContext(), "Deleting " + swipedBook.getTitle(), Toast.LENGTH_LONG).show();

                        bookViewModel.delete(swipedBook);
                    }
                }
        );

        helper.attachToRecyclerView(recyclerView);
       // finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        boolean check_result_code = resultCode == Activity.RESULT_OK;

        if(MainActivity.SELECTED_BOOK_REQUEST == requestCode && check_result_code){

            String reply = data.getStringExtra(  SelectedBookActivity.REPLY_KEY );

            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
        }

    }
}