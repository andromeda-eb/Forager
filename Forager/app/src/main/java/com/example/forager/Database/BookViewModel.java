package com.example.forager.Database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository repository;

    private LiveData<List<Book>> allBooks;

    public BookViewModel(Application application){
        super(application);
        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() { return allBooks; }

    public void insert(Book book) { repository.insert(book);}

    public void delete(Book book){ repository.delete(book); }
}
