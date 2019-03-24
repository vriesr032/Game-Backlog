package com.example.gamebacklog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private GameListAdapter gameListAdapter;
    private RecyclerView rvGameList;
    private List<Game> gameList = new ArrayList<>();
    private GestureDetector gestureDetector;
    private GameRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = GameRoomDatabase.getDatabase(this);


        initRecyclerView();
        getAllGames();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        gameListAdapter = new GameListAdapter(gameList);
        rvGameList = findViewById(R.id.rv_game);
        rvGameList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvGameList.setAdapter(gameListAdapter);
        rvGameList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = rvGameList.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = rvGameList.getChildAdapterPosition(child);
                    game = gameList.get(adapterPosition);
                    Intent intent = new Intent(MainActivity.this, EditGameActivity.class);
                    intent.putExtra("game", game.getId());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getX() - e2.getX() > 50 || e2.getX() - e1.getX() > 50){
                    View child = rvGameList.findChildViewUnder(e1.getX(), e1.getY());
                    if (child != null) {
                        int adapterPosition = rvGameList.getChildAdapterPosition(child);
                        deleteGame(gameList.get(adapterPosition));
                    }
                    return true;
                }

                if (e1.getY() < e2.getY()) {
                    getAllGames();
                }

                return true;
            }
        });

        rvGameList.addOnItemTouchListener(this);
        getAllGames();
    }

    private void updateUI(List<Game> games) {
        gameList.clear();
        gameList.addAll(games);
        gameListAdapter.notifyDataSetChanged();
    }

    public void getAllGames() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Game> games = db.gameDao().getAllGames();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(games);
                    }
                });
            }
        });
    }

    private void deleteGame(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gameDao().delete(game);
                getAllGames();
            }
        });
    }

    private void deleteAllGames(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gameDao().delete(games);
                getAllGames();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteAllGames(gameList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
