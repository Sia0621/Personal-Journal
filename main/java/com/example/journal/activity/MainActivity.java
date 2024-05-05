package com.example.journal.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.journal.Journal;
import com.example.journal.JournalDbOpenHelper;
import com.example.journal.R;
import com.example.journal.recyclerview.JournalRecyclerAdatpter;

import java.text.SimpleDateFormat;
import java.util.List;

/*
*  1. I combined toolbar and menu to realise the feature of
*     creating, saving, edit, deleting journals.
*  2. I used the dialog feature to ask users whether to
*     delete this journal.
*  3. I used the location service to get location automatically.
*  4. I changed app icon and splash page.
* */


public class MainActivity extends AppCompatActivity {

    JournalDbOpenHelper db = new JournalDbOpenHelper(this);
    RecyclerView recyclerView;
    JournalRecyclerAdatpter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview);


        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // require location permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        List<Journal> journals = db.getAllJournals();

        //adapter = new JournalRecyclerAdatpter(journals, this);
        adapter = new JournalRecyclerAdatpter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //reference
        //add spaces between items
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int count = parent.getAdapter().getItemCount();
                int index = parent.getChildAdapterPosition(view);
                if (index < count - 1) {
                    outRect.set(0, 0, 0, 50);
                }
            }

        });

        adapter.setOnItemClick(new JournalRecyclerAdatpter.MyItemClick() {
            @Override
            public void onItemClick(Journal journal, int postion) {

                Intent intent = new Intent(MainActivity.this, JournalBrowseActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("id", journal.getId());
                bundle.putString("date", new SimpleDateFormat("yyyy-MM-dd").format(journal.getDate()));
                bundle.putString("weather", journal.getWeather());
                bundle.putString("title", journal.getTitle());
                bundle.putString("content", journal.getContent());
                bundle.putString("address", journal.getAddress());

                intent.putExtras(bundle);

                startActivityForResult(intent, 2);

            }
        });

        recyclerView.setAdapter(adapter);

        adapter.setNewData(journals);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_create, menu);
        //true means displaying the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_create) {
            Intent intent = new Intent(this, JournalEditActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("journal_id", 0);
            intent.putExtras(bundle);

            startActivityForResult(intent, 1);

            //this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    //Toast.makeText(this, "execute？", Toast.LENGTH_SHORT).show();
                    List<Journal> journals = db.getAllJournals();
                    adapter.setNewData(journals);
                }
                break;

            case 2:
                if (resultCode == RESULT_OK){
                    //Toast.makeText(this, "execute 2？", Toast.LENGTH_SHORT).show();
                    List<Journal> journals = db.getAllJournals();
                    adapter.setNewData(journals);
                }
                break;
        }

    }
}