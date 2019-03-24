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

public class EditGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<Game> gameList = new ArrayList<>();
    private EditText title;
    private EditText platform;
    private Spinner status;
    private String Title;
    private String Platform;
    private String Status;
    private String Date;
    private Date atm = Calendar.getInstance().getTime();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Executor executor = Executors.newSingleThreadExecutor();
    private GameRoomDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);
        Toolbar toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);

        title = findViewById(R.id.edit_title);
        platform = findViewById(R.id.edit_platform);
        status = findViewById(R.id.edit_spinner);
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

        FloatingActionButton update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUpdateGame();
                finish();
            }
        });
    }

    private void initUpdateGame(){
        int gameid = (int) getIntent().getSerializableExtra("game");
        Game game = new Game(Title, Platform, Status, Date);
        game.setId(gameid);
        game.setTitle(title.getText().toString());
        game.setPlatform(platform.getText().toString());
        game.setStatus(status.getSelectedItem().toString());
        game.setDate(dateFormat.format(atm));
        updateGame(game);
    }

    private void updateGame(final Game game){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gameDao().update(game);
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
        getMenuInflater().inflate(R.menu.menu_edit_game, menu);
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
