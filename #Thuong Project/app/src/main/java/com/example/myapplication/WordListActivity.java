package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.adapter.WordListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordListActivity extends AppCompatActivity {
    private List<String> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_action);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        WordListAdapter wordListAdapter = new WordListAdapter(this, getWordList());
        recyclerView.setAdapter(wordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //wordListAdapter.notifyDataSetChanged(); => method used to update RecycleView
    }

//    private List getWordList() {
//        for (int i = 0; i <= 50; i++) {
//            wordList.add("Word " + i);
//        }
//        return wordList;
//    }


    //17022023 do data storage
    private List getWordList() {
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.words));
        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            wordList.add(word);
        }
        scanner.close();
        return wordList;
    }
}