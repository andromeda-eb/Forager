package com.example.forager.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookRoomDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

    private static BookRoomDatabase INSTANCE;

    // singleton to prevent multiple instances
    public static BookRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookRoomDatabase.class) {
                if (INSTANCE == null) {
                    // create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookRoomDatabase.class, "book_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
