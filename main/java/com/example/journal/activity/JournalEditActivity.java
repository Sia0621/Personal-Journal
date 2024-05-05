package com.example.journal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journal.Journal;
import com.example.journal.JournalDbOpenHelper;
import com.example.journal.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalEditActivity extends AppCompatActivity implements LocationListener {

    JournalDbOpenHelper db = new JournalDbOpenHelper(this);
    int id;
    ArrayAdapter<String> adapter;
    DatePickerDialog datePickerDialog;
    TextView dateTextView;
    Spinner weatherSpinner;
    EditText titleEditText;
    EditText contentEditText;
    TextView locationTextView;

    private Double longitude,latitude;

    private LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_edit);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dateTextView = findViewById(R.id.date_textView);
        weatherSpinner = findViewById(R.id.weather_spinner);
        adapter = new ArrayAdapter(this,R.layout.spinner_row,
                R.id.row, getResources().getStringArray(R.array.option));
        weatherSpinner.setAdapter(adapter);

        titleEditText = findViewById(R.id.title_editText);
        contentEditText = findViewById(R.id.content_editText);
        locationTextView = findViewById(R.id.locationTextView);

        locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

        });

        id = getIntent().getExtras().getInt("journal_id", 0);

        if (id == 0){
            toolbar.setTitle("Create");
            dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }else {
            Journal journal = db.selectJournal(id);
            dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd").format(journal.getDate()));
            weatherSpinner.setSelection(adapter.getPosition(journal.getWeather()));
            titleEditText.setText(journal.getTitle());
            contentEditText.setText(journal.getContent());
            locationTextView.setText(journal.getAddress());
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // get LocationManager object
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // start location permission
        // LocationManager.GPS_PROVIDER GPS location
        // LocationManager.NETWORK_PROVIDER network location
        // LocationManager.PASSIVE_PROVIDER passive location
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, JournalEditActivity.this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(JournalEditActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            String info = address.getAddressLine(0);
            locationTextView.setText(info);
            //Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
        Log.e("onStatusEnables", provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
        Log.e("onProviderDisabled", provider);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_save, menu);
        //true means displaying the menu
        return true;
    }

    //return to browse activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_save) {
            //Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
            String date = dateTextView.getText().toString();
            if (date.isEmpty()) {
                Toast.makeText(this, "Please select the date", Toast.LENGTH_SHORT).show();
            }
            String weather = weatherSpinner.getSelectedItem().toString();
            if (weather.isEmpty()) {
                Toast.makeText(this, "Please select the weather", Toast.LENGTH_SHORT).show();
            }
            String title = titleEditText.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Please input the title", Toast.LENGTH_SHORT).show();
            }
            String content = contentEditText.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Please input the content", Toast.LENGTH_SHORT).show();
            }

            String location = locationTextView.getText().toString().trim();
            if (location.isEmpty()) {
                Toast.makeText(this, "Please input the location", Toast.LENGTH_SHORT).show();
            }

            Journal journal = new Journal();
            try {
                journal.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            } catch (ParseException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
            journal.setWeather(weather);
            journal.setTitle(title);
            journal.setContent(content);
            journal.setAddress(location);

            if (id > 0) {
                db.updateJournal(id, journal);
            } else {
                db.createJournal(journal);
            }
            Toast.makeText(this, "Save successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();

        }else{
            finish();
            //onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //I referred this method to create a calendar
    /**
     * select the date from calendar
     */
    void showDatePickerDialog() {
        //reference
        if (datePickerDialog == null) {
            Calendar calendar = Calendar.getInstance();
            try {
                String dateStr = dateTextView.getText().toString().trim();
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                calendar.setTime(date);
            } catch (ParseException e) {
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(JournalEditActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Date date = getDate(year, monthOfYear, dayOfMonth);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    dateTextView.setText(dateStr);
                }

            }, year, month, day);
        }
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    private Date getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar.getTime();
    }


}