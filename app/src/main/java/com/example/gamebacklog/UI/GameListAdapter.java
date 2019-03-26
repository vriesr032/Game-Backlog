package com.example.gamebacklog.UI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gamebacklog.Model.Game;
import com.example.gamebacklog.R;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private List<Game> gameList;

    public GameListAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.grid_cell, viewGroup, false);
        return new GameListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.gameTitle.setText(gameList.get(viewHolder.getAdapterPosition()).getTitle());
        viewHolder.gamePlatform.setText(gameList.get(viewHolder.getAdapterPosition()).getPlatform());
        viewHolder.status.setText(gameList.get(viewHolder.getAdapterPosition()).getStatus());
        viewHolder.date.setText(gameList.get(viewHolder.getAdapterPosition()).getDate());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void swapList (List<Game> newList) {
        gameList = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView gameTitle;
        private TextView gamePlatform;
        private TextView status;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.tv_title);
            gamePlatform = itemView.findViewById(R.id.tv_platform);
            status = itemView.findViewById(R.id.tv_status);
            date = itemView.findViewById(R.id.tv_date);
        }
    }
}
