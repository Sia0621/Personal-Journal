package com.example.journal.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journal.Journal;
import com.example.journal.JournalDbOpenHelper;
import com.example.journal.R;

import java.text.SimpleDateFormat;


public class JournalBrowseActivity extends AppCompatActivity {

    JournalDbOpenHelper db = new JournalDbOpenHelper(this);
    int id;
    TextView titleTextView;
    TextView dateTextView;
    TextView weatherTextView;
    TextView contentTextView;
    TextView locationTextView;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 1: {
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(this, "excute?", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Journal journal = db.selectJournal(id);
                    titleTextView.setText(journal.getTitle());
                    dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(journal.getDate()));
                    weatherTextView.setText(journal.getWeather());
                    contentTextView.setText(journal.getContent());
                    locationTextView.setText(journal.getAddress());
                }
            }
            break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_browse);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        titleTextView = findViewById(R.id.title_tv);
        dateTextView = findViewById(R.id.date_tv);
        weatherTextView = findViewById(R.id.weather_tv);
        contentTextView = findViewById(R.id.content_tv);
        locationTextView = findViewById(R.id.location_tv);

        Bundle b = getIntent().getExtras();

        titleTextView.setText(b.getString("title", ""));
        dateTextView.setText(b.getString("date", ""));
        weatherTextView.setText(b.getString("weather", ""));
        contentTextView.setText(b.getString("content", ""));
        locationTextView.setText(b.getString("address", ""));

        id = b.getInt("id", 0);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_browse, menu);

        //true means displaying the menu
        return true;
    }

    //return to browse activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, JournalEditActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("journal_id", id);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            //this.startActivity(intent);
        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("Delete this journal?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(JournalBrowseActivity.this, "d", Toast.LENGTH_SHORT).show();
                    db.deleteJournal(id);
                    Toast.makeText(JournalBrowseActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(JournalBrowseActivity.this, "c", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}