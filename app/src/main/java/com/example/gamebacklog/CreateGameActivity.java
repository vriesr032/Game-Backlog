package com.example.gamebacklog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText title;
    private EditText platform;
    private Spinner status;
    private Date atm = Calendar.getInstance().getTime();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Executor executor = Executors.newSingleThreadExecutor();
    private GameRoomDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        title = findViewById(R.id.create_title);
        platform = findViewById(R.id.create_platform);
        status = findViewById(R.id.create_spinner);
        db = GameRoomDatabase.getDatabase(this);

        status.setOnItemSelectedListener(this);

        List<String> states = new ArrayList<String>();
        states.add("Want to play");
        states.add("Playing");
        states.add("Stalled");
        states.add("Dropped");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(dataAdapter);

        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCreateGame();
            }
        });
    }

    private void initCreateGame(){
        String Title = title.getText().toString();
        String Platform = platform.getText().toString();
        String Status = status.getSelectedItem().toString();
        String Date = dateFormat.format(atm);
        final Game game = new Game(Title, Platform, Status, Date);
        insertGame(game);
        finish();
    }

    private void insertGame(final Game game){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gameDao().insert(game);
                runOnUiThread(new Runnable() { // Optionally clear the text from the input field
                    @Override
                    public void run() {
                        title.setText("");
                        platform.setText("");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
