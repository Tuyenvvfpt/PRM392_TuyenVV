package com.example.myapplication.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.entity.Word;

@Database(entities = {Word.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDAO wordDAO();

    private static WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(Context context) {
        synchronized (WordRoomDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, WordRoomDatabase.class, " WordRoomDatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();
            }
        }
        return INSTANCE;
    }
}
