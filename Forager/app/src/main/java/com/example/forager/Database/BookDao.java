package com.example.forager.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Query("SELECT * FROM book_table")
    LiveData<List<Book>> getAllBooks();

    @Delete
    void delete(Book book);
}