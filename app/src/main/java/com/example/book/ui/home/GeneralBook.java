package com.example.book.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.book.R;
import com.example.book.ui.Adapter.BookAdapter;

import java.util.ArrayList;

public class GeneralBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_book);

        // Assuming you have initialized ViewModelProvider properly
        GeneralViewModel generalViewModel = new ViewModelProvider(this).get(GeneralViewModel.class);


        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter
        BookAdapter bookAdapter = new BookAdapter(new ArrayList<>()); // Pass an empty list initially
        recyclerView.setAdapter(bookAdapter);

        // Observe the LiveData from ViewModel and update UI when data changes
        generalViewModel.getPosts().observe(this, posts -> {
            // Update RecyclerView adapter with the new data
            bookAdapter.setData(posts);
        });
    }
}
