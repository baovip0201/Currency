package com.bao.currency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bao.currency.Controller.Database;
import com.bao.currency.Model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private HistoryAdapter recyclerAdapter;
    private RecyclerView mRecyclerView;
    Database database;
    public static List<History> historyList=new ArrayList<>();
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        database=new Database(this);
        initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HistoryAdapter(getApplicationContext(), historyList);
        mRecyclerView.setAdapter(recyclerAdapter);
        getData();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                historyList=new ArrayList<>();
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void initView() {
        mRecyclerView=findViewById(R.id.recycler_view);
        img_back=findViewById(R.id.back);
    }

    private void getData() {
        Cursor cursor=database.readAllData();
        if(cursor.getCount()==0){
            System.out.println("No data");
        }else {
            while (cursor.moveToNext()){
                String id=cursor.getString(0);
                String date=cursor.getString(5);
                String from_rate=cursor.getString(1);
                String to_rate=cursor.getString(2);
                String _from=cursor.getString(3);
                String _out=cursor.getString(4);
                String result=_from+" "+from_rate.toUpperCase()+" = "+_out+" "+to_rate.toUpperCase();
                historyList.add(new History(id, result, date));
            }
        }
    }

}