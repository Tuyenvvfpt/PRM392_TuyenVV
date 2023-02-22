package com.example.myapplication.repository;

import android.content.Context;

import com.example.myapplication.dao.WordDAO;
import com.example.myapplication.dao.WordRoomDatabase;
import com.example.myapplication.entity.Word;

import java.util.List;

public class WordRepository {
    private WordDAO wordDAO;
    public WordRepository(Context context){
        WordRoomDatabase wordRoomDatabase = WordRoomDatabase.getDatabase(context);
        wordDAO = wordRoomDatabase.wordDAO();
    }
    public List<Word> getAllWords(){
        return wordDAO.getAllWords();
    }
    public void insertWord(Word word){
        wordDAO.insert(word);
    }
}
