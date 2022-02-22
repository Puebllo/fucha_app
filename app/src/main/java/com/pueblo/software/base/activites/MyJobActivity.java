package com.pueblo.software.base.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pueblo.software.base.adapter.MyJobAdapter;
import com.pueblo.software.base.common.CommonVariables;

import software.pueblo.com.fucha.R;

public class MyJobActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);

        recyclerView = findViewById(R.id.my_job_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyJobAdapter(this, CommonVariables.myJobs);
        recyclerView.setAdapter(adapter);

    }
}
