package com.example.forager.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    BookRepository(Application application){
        BookRoomDatabase db = BookRoomDatabase.getDatabase(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    // returns cached books as live data
    LiveData<List<Book>> getAllBooks(){
        return allBooks;
    }

    // calls on non ui thread (crash otherwise)
    public void insert (Book book) {
        new insertAsyncTask(bookDao).execute(book);
    }

    public void delete(Book book){
        new deleteAsyncTask(bookDao).execute(book);
    }

    private static class insertAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao asyncTaskDao;

        insertAsyncTask(BookDao dao){ asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Book... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao asyncTaskDao;

        deleteAsyncTask(BookDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Book... params){
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
