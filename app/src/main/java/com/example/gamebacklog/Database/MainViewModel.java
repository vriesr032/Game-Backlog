package com.example.gamebacklog.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.gamebacklog.Model.Game;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private GameRepository gameRepository;
    private LiveData<List<Game>> gameList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        gameRepository = new GameRepository(application.getApplicationContext());
        gameList = gameRepository.getAllGames();
    }

    public LiveData<List<Game>> getGames() {
        return gameList;
    }

    public void insert(Game game) {
        gameRepository.insert(game);
    }

    public void update(Game game) {
        gameRepository.update(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

    public void deleteAllGames(List<Game> games){
        gameRepository.deleteAllGames(games);
    }
}
