package com.example.gamebacklog.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.gamebacklog.Model.Game;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {

    private GameRoomDatabase gameRoomDatabase;
    private GameDao gameDao;
    private LiveData<List<Game>> gameList;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameRepository(Context context) {
        gameRoomDatabase = GameRoomDatabase.getDatabase(context);
        gameDao = gameRoomDatabase.gameDao();
        gameList = gameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return gameList;
    }

    public void insert(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.insert(game);
            }
        });
    }

    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.update(game);
            }
        });
    }

    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.delete(game);
            }
        });
    }

    public void deleteAllGames(final List<Game> games) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.delete(games);
            }
        });
    }
}
